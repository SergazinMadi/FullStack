package org.example.fullstack.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.fullstack.db.enums.UserRole;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    @Column(length = 20)
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "license_number", length = 50)
    private String licenseNumber; // Номер водительского удостоверения
    
    @Column(name = "vehicle_type", length = 50)
    private String vehicleType; // Тип транспорта (фура, газель, легковой)
    
    @Column(name = "vehicle_number", length = 20)
    private String vehicleNumber; // Номер транспорта
    
    @Column(name = "max_load_capacity")
    private Double maxLoadCapacity; // Максимальная грузоподъемность (кг)
    
    @Column(columnDefinition = "TEXT")
    private String address;
    
    @ManyToOne()
    @JoinColumn(name = "city_id")
    private City city;

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() { return true;}
}

