package org.example.fullstack.db.enums;

import lombok.Getter;

@Getter
public enum PickupPointType {
    POSTAL_OFFICE("Почтовое отделение"),
    PICKUP_LOCKER("Постамат"),
    STORE_PICKUP("Магазин-партнер"),
    WAREHOUSE("Склад"),
    GAS_STATION("АЗС"),
    SHOPPING_MALL("ТЦ");
    
    private final String displayName;
    
    PickupPointType(String displayName) {
        this.displayName = displayName;
    }

}