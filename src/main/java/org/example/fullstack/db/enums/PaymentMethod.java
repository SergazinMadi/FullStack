package org.example.fullstack.db.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CASH("Наличные"),
    CARD("Банковская карта"),
    BANK_TRANSFER("Банковский перевод"),
    ELECTRONIC_WALLET("Электронный кошелек"),
    CRYPTO("Криптовалюта");
    
    private final String displayName;
    
    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
}