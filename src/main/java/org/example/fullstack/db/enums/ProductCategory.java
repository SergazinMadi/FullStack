package org.example.fullstack.db.enums;

import lombok.Getter;

@Getter
public enum ProductCategory {
    DOCUMENTS("Документы"),
    CLOTHING("Одежда"),
    ELECTRONICS("Электроника"),
    FOOD("Продукты питания"),
    MEDICINE("Медикаменты"),
    FRAGILE("Хрупкие товары"),
    VALUABLES("Ценные вещи"),
    FURNITURE("Мебель"),
    APPLIANCES("Бытовая техника"),
    BOOKS("Книги"),
    OTHER("Прочее");
    
    private final String displayName;
    
    ProductCategory(String displayName) {
        this.displayName = displayName;
    }
}