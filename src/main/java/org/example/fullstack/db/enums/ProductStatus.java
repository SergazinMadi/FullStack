package org.example.fullstack.db.enums;

import lombok.Getter;

@Getter
public enum ProductStatus {
    CREATED("Заказ создан"),
    READY_FOR_PICKUP("Готов к забору"),
    IN_TRANSIT("В пути"),
    AT_PICKUP_POINT("В пункте выдачи"),
    DELIVERED("Доставлен"),
    RETURNED("Возвращен"),
    LOST("Утерян"),
    DAMAGED("Поврежден");

    private final String displayName;
    
    ProductStatus(String displayName) {
        this.displayName = displayName;
    }

}