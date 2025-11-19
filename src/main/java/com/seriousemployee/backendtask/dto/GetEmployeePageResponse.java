package com.seriousemployee.backendtask.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record GetEmployeePageResponse(
        List<GetEmployeeResponse> employees,
        int currentPage,
        int totalPages,
        long totalItems
) {
    public static GetEmployeePageResponse fromPage(Page<GetEmployeeResponse> page) {
        return new GetEmployeePageResponse(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
