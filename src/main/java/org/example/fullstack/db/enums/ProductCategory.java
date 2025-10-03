package org.example.fullstack.db.enums;

import lombok.Getter;

@Getter
public enum ProductCategory {
    DOCUMENTS("Документы"),
    ELECTRONICS("Электроника"),
    CLOTHING("Одежда"),
    FOOD("Продукты питания"),
    FURNITURE("Мебель"),
    AUTOMOTIVE("Автозапчасти"),
    BOOKS("Книги"),
    HOUSEHOLD("Бытовые товары"),
    OTHER("Прочее");
    
    private final String displayName;
    
    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

}
