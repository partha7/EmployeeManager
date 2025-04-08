package com.demo.employeemanager.models.enums;

import lombok.Getter;

@Getter
public enum SortOrder {
    DESC("desc"),
    ASC("asc");

    private final String value;

    SortOrder(String value) {
        this.value = value;
    }

}
