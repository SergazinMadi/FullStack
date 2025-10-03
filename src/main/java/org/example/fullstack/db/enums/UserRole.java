package org.example.fullstack.db.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    CLIENT("Клиент"),
    COURIER("Курьер"),
    DRIVER("Водитель"), 
    MANAGER("Менеджер пункта выдачи"),
    ADMIN("Администратор");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }

}