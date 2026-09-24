# Dachser Financial Logistics Dashboard

A full-stack financial tracking and logistics dashboard designed for Dachser. The system enables logistics operations teams to manage shipments, track granular operational costs and revenues, compute real-time profit and loss (P&L), and audit historical financial calculations through an immutable ledger.

---

## 1. Problem Statement

Logistics and freight-forwarding workflows require financial visibility at the individual shipment level. Real-world shipping operations rarely involve static, one-time charges; shipments regularly accrue dynamic costs across multiple legs, such as base carrier fees, demurrage, fuel surcharges, and customs clearance. Similarly, billing may encompass base rates alongside negotiated client surcharges, often involving multiple currencies in international logistics.

### Core Objectives

- Provide an operational overview of all active and delivered shipments with server-side pagination and dynamic searching.
- Enable granular registration of revenue entries and operational cost items, explicitly tracking the currency for each financial event.
- Compute and record point-in-time financial snapshots (Income, Costs, Profit/Loss) without mutating prior records.
- Maintain clean architectural boundaries on both the backend and frontend to support enterprise maintainability and auditability.

---

## 2. Database Schema & Domain Modeling

The database is designed around third normal form (3NF) principles with specialized snapshotting tables to balance data normalization with historical auditing requirements. Multi-currency support is built directly into the financial models.

### Schema Breakdown & Rationale

`users`

**Rationale**: Essential for enterprise-grade auditability and compliance. In logistics finance, tracking who generated a financial snapshot or authorized an additional cost is just as critical as tracking what changed. By linking the `incomes`, `costs`, and `shipment_financials` tables to a specific operator via the `created_by` field, the system maintains a strict, unalterable audit trail. Modeling this identity relationship from day one ensures the database is fully prepared for future Identity and Access Management (IAM) integration.

`customers`

**Rationale**: Separates customer identity and metadata from physical freight tracking. This prevents data duplication and ensures that customer metadata updates (e.g., tax ID changes) apply globally without altering existing shipment records.

`shipments`

**Rationale**: Represents the physical movement of freight. It acts as the central anchor for tracking numbers, operational lifecycle statuses (`CREATED`, `IN_TRANSIT`, `DELIVERED`), and links directly to the customer entity.

`incomes`

**Rationale**: Stores individual revenue events related to a shipment. By isolating incomes, we can accurately track discrete billing events (e.g., base rates vs. negotiated surcharges), explicitly track the currency of the payment, and handle active or voided statuses.

`costs`

**Rationale**: Tracks granular, itemized operational costs. Logistics finance requires distinguishing between cost classifications (e.g., `BASE_COST` vs. `ADDITIONAL_COST`). Storing single aggregated totals on the shipment table would destroy itemized auditability and prevent partial disputes or subsequent cost allocations.

`shipment_financials` (The Immutable Ledger)

**Rationale (Why not compute on the fly?)**: Computations derived purely on the fly create a moving target for accounting. If an operator adjusts a line item weeks after delivery, historical reporting would shift silently. The `shipment_financials` table serves as an audit ledger: each entry preserves the exact financial state (Income, Costs, P&L, Currency) as it was evaluated at that moment in time, hard-linked to the specific income and cost IDs that generated it.

### The Necessity of Isolated Incomes, Costs, and Plural Linkages

#### Why Separate Incomes and Costs?

In real-world logistics, the financial profile of a shipment is rarely a single static invoice and a single static carrier fee. A shipment might generate revenue from a base negotiated rate, but later incur additional revenues like expedited service fees or special handling charges. Similarly, operational costs arrive in fragments: a base transport cost, a subsequent fuel surcharge, a customs clearance fee, or a demurrage fine.

By isolating incomes and costs into their own dedicated tables, we create a highly flexible, append-only architecture. Every financial event is itemized, categorized (`BASE_COST`, `ADDITIONAL_COST`), and tracked independently with its own status and currency.

#### Why shipment_financials Uses Plural IDs (`income_ids`, `cost_ids`)

The `shipment_financials` table serves as a point-in-time snapshot (a ledger entry) of a shipment's profitability. Because a shipment has multiple fragmented costs and incomes, a single P&L calculation must aggregate several rows at once. By storing arrays of IDs (`income_ids`, `cost_ids`), the financial snapshot creates a strict audit trail. If a snapshot declares a $500 profit, the plural IDs act as a receipt, proving exactly which specific line items were included in that mathematical result at that specific moment.

#### Flexibility for Future Alterations

This design accommodates the volatile nature of freight billing. If a late customs fine arrives three weeks after a shipment is delivered, the system does not need to alter past records. It simply inserts a new row into the costs table and triggers a new calculation. The new `shipment_financials` record will link to the original `income_ids` and the updated `cost_ids`, capturing the new profit margin without erasing the historical record of what the profit used to be.

#### What Would Happen If These Tables Didn't Exist?

If we collapsed this data directly into the shipments table (e.g., using flat `total_income` and `total_costs` columns), we would create a destructive, overwrite-heavy system. Every time a new fee arrived, the previous totals would be overwritten and lost forever. If a customer disputed a bill, there would be no itemized database record explaining how the final total was reached. Alternatively, if we tried to store everything in `shipment_financials` without separate line-item tables, we would be forced to create rigid, hardcoded columns (e.g., `cost_1`, `cost_2`, `cost_3`) or duplicate massive amounts of textual data.

#### So, do We Really Need These Tables?

Yes. For enterprise logistics, financial auditability is mandatory. Without independent incomes and costs tables securely linked by arrays in a snapshot ledger, the application could not support partial billing disputes, historical P&L auditing, or the dynamic addition of post-delivery fees.

## 3. Backend Architecture (Spring Boot)

The backend is built with Spring Boot using an N-Tier Clean Architecture pattern, isolating concerns across Controllers, Services, Repositories, and Domain Mappers.

### Database Migrations with Flyway

Instead of relying on Hibernate's `ddl-auto` properties (which can be unpredictable and dangerous in production environments), we utilize Flyway for database version control.

- **Benefits**: Flyway ensures deterministic, reproducible schema evolutions across all environments. It tracks which migration scripts have been applied, guaranteeing that the database structure perfectly matches the application's expectations on startup.

### Layer Separation

1. **Controller Layer**: Acts as an HTTP gateway. Responsible for request routing, query parameter parsing, input validation (`@Valid`), and mapping execution results to standard HTTP status codes. Contains no business or calculation logic.

2. **Service Layer**: Houses the core business rules, transactional boundaries (`@Transactional`), and calculation pipelines. Evaluates financial commands, coordinates domain entities, processes currency alignments, and handles audit record generation.

3. **Repository Layer**: Encapsulates database communication through Spring Data JPA interfaces.

### Deliberate Avoidance of JPA Relationship Annotations

A conscious architectural decision was made to avoid complex JPA relationship annotations (such as `@OneToMany`, `@ManyToOne`, or `@ManyToMany`). Instead of building deeply nested object graphs (e.g., a `ShipmentEntity` containing a `List<CostEntity>`), entities reference each other strictly by their database IDs (e.g., `Long customerId`, `Long createdBy`).

- **Elimination of "Hibernate Magic"**: JPA relationships often introduce hidden performance traps, most notably the N+1 query problem or unexpected `LazyInitializationException` errors. By storing raw IDs, the application retains absolute control over when and how data is fetched.
- **Explicit Querying**: When related data is needed (such as fetching a User's name to enrich a financial record), we utilize explicit JPQL projection queries alongside `@Transient` fields. This ensures we only query exactly what we need—nothing more, nothing less.
- **Readability and Debugging**: While this approach requires slightly more explicit code to manually link or query related data, it drastically simplifies debugging. An entity maps 1:1 with its database table. There are no infinite recursion bugs during JSON serialization, no complex `CascadeType` rules to memorize, and domain boundaries remain clear.

### Entity vs. DTO Encapsulation & MapStruct

A strict boundary is maintained between database persistence models (JPA Entities) and API contracts (DTOs / Records). To achieve this without writing tedious, error-prone boilerplate mapping code, we utilize MapStruct.

- **Why MapStruct?**: MapStruct generates plain Java method invocations at compile time to map between Entities and DTOs. Unlike reflection-based mappers (e.g., ModelMapper), MapStruct is highly performant, type-safe, and allows for easy debugging.

- **Entities Stay in the Persistence Layer**: JPA Entities (`ShipmentEntity`, `CostEntity`, etc.) are mapped to Domain Models or DTOs before crossing the boundary into the application's service return signatures.

- **Pros**:
  - **Security**: Prevents unintended data exposure (e.g., internal sequence IDs, database flags, or audit fields).
  - **Decoupling**: Database schema refactors do not break external API consumers as long as the mapper preserves the API contract.
  - **Session Safety**: Prevents serialization crashes outside the active transaction context.

### Dynamic Querying (Specifications)

Rather than writing static finder methods for every conceivable filter combination, the shipment catalog uses dynamic queries powered by Spring Data JPA Specifications.

- **Mechanism**: Accepts dynamic criteria (such as `filter.search` and Spring `Pageable` options) to compose SQL `WHERE` clauses on the fly.
- **Benefits**: Consolidates complex multi-field searches (tracking numbers, customer names) into a single unified query pipeline, keeping repository interfaces minimal and maintainable.

## 4. Frontend Architecture (Angular 17)

The frontend is implemented as a Single Page Application (SPA) using Angular 17, Bootstrap 5 utilities, and Angular Material components.

### Standalone Component Architecture

The application eliminates `NgModule` declarations entirely, utilizing Angular 17's standalone component architecture:

- Every feature component explicitly declares its required dependencies (`imports: [CommonModule, MatTableModule, ...]`).
- Streamlines code splitting, simplifies dependency tracking, and facilitates lazy-loading across routes.

### UI & Modal Workflows

- **Modal-Driven Operations**: Financial calculations are extracted from the main page body into a dedicated Angular Material Dialog (`CalculationDialogComponent`).
  - **Reasoning**: Storing calculation forms inline degrades vertical readability on high-density data tables. The dialog modal keeps the main detail screen dedicated strictly to historical ledger analysis, opening user input controls only on demand.

- **Component File Separation**: Complex components (such as calculation dialogs and view pages) maintain strict physical separation between logic (`.ts`), templates (`.html`), and component-scoped styling (`.scss`).

### Configuration & Multi-Environment Deployments

- All network targets use Angular's environment abstraction (`src/environments/environment.ts` and `environment.development.ts`).
- Services inject `environment.apiUrl` dynamically rather than hardcoding host endpoints. This permits straightforward CI/CD build swaps across development, staging, and production environments.

### Route State & Browser History Management

Pagination and search terms are two-way synchronized with browser URL parameters (`?page=...&size=...&filter.search=...`).

- **Deep Linking**: Users can share or bookmark filtered shipment queries directly.
- **History Stack Preservation** (`replaceUrl: true`): When updating pagination or adjusting page sizes on detail views, navigation calls apply `{ replaceUrl: true }`. This prevents every page-click or filter change from polluting the browser's history stack, ensuring that the browser "Back" button returns the user directly to the preceding view.

## 5. Local Setup & Execution

### Prerequisites

- **Java 17+**
- **Node.js 18+ and npm**

### Backend (Spring Boot)

Nothing is needed for the database, since we use Flyway for the migrations alongside an embedded H2 database.

1. Start the application:

   ```bash
   ./mvnw spring-boot:run
   ```

   The server defaults to port 8090.

### Frontend (Angular)

1.  Navigate to the frontend directory:

    ```Bash
    cd frontend
    ```

2.  Install dependencies:

    ```Bash
    npm install
    ```

3.  Start the local development server:

    ```Bash
    npx ng serve
    ```

    Access the dashboard at `localhost:4200`
