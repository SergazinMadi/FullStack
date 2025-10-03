package org.example.fullstack.db.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("Создан"),
    ASSIGNED_TO_DRIVER("Назначен водителю"),
    PICKUP_IN_PROGRESS("Забор в процессе"),
    PICKED_UP("Забран"),
    IN_TRANSIT("В пути"),
    AT_PICKUP_POINT("В пункте выдачи"),
    OUT_FOR_DELIVERY("На доставке"),
    DELIVERED("Доставлен"),
    RETURNED_TO_SENDER("Возвращен отправителю"),
    CANCELLED("Отменен"),
    LOST("Утерян"),
    DAMAGED("Поврежден");
    
    private final String displayName;
    
    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

}