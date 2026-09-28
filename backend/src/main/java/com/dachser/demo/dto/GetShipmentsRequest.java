package com.dachser.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetShipmentsRequest {

    private GetShipmentsFilter filter = new GetShipmentsFilter();
    private Integer page = 0;
    private Integer size = 20;

    public GetShipmentsRequest() {
        this.filter = new GetShipmentsFilter();
        this.page = 0;
        this.size = 20;
    }
}