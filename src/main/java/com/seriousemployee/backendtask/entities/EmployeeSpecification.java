package com.seriousemployee.backendtask.entities;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EmployeeSpecification {
    public static Specification<Employee> build(
            String role,
            LocalDate before,
            LocalDate after
    ) {
        Specification<Employee> spec = (root, query, builder) -> null;

        if (role != null)
            spec = spec.and(hasRole(role));
        if (before != null)
            spec = spec.and(joinedBefore(before));
        if (after != null)
            spec = spec.and(joinedAfter(after));

        return spec;
    }

    private static Specification<Employee> hasRole(String role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null || role.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("role"), role);
        };
    }

    private static Specification<Employee> joinedBefore(LocalDate before) {
        return (root, query, criteriaBuilder) -> {
            if (before == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), before);
        };
    }

    private static Specification<Employee> joinedAfter(LocalDate after) {
        return (root, query, criteriaBuilder) -> {
            if (after == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThan(root.get("createdAt"), after);
        };
    }
}
