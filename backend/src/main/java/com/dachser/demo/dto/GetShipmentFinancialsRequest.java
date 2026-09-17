package com.dachser.demo.dto;


import lombok.NonNull;

public record GetShipmentFinancialsRequest(@NonNull Long shipmentId, Integer page, Integer size) {
    public GetShipmentFinancialsRequest {
        if (page == null) page = 0;
        if (size == null) size = 20;
    }
}
