package org.example.fullstack.db.enums;

import lombok.Getter;

@Getter
public enum PickupPointType {
    WAREHOUSE("Склад"),
    RETAIL_STORE("Розничный магазин"),
    POST_OFFICE("Почтовое отделение"),
    COURIER_OFFICE("Офис курьерской службы"),
    MOBILE_POINT("Мобильный пункт"),
    HOME_DELIVERY("Адресная доставка");
    
    private final String displayName;
    
    PickupPointType(String displayName) {
        this.displayName = displayName;
    }
}