package com.dachser.demo.dto;

public record GetShipmentsRequest(
        GetShipmentsFilter filter,
        Integer page,
        Integer size
) {
    public GetShipmentsRequest {
        if (page == null) page = 0;
        if (size == null) size = 20;
        if (filter == null) filter = new GetShipmentsFilter(null, null, null);
    }
}