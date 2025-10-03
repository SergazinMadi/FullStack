package org.example.fullstack.db.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CASH("Наличные"),
    CARD("Банковская карта"),
    ONLINE("Онлайн-оплата"),
    BANK_TRANSFER("Банковский перевод"),
    PREPAID("Предоплата");
    
    private final String displayName;
    
    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

}