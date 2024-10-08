package org.example.catch_line.user.owner.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.common.model.converter.PasswordConverter;
import org.example.catch_line.common.model.converter.PhoneNumberConverter;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;

@Table(name = "owner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class OwnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ownerId;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Convert(converter = PasswordConverter.class)
    private Password password;

    @Column(nullable = false)
    @Convert(converter = PhoneNumberConverter.class)
    private PhoneNumber phoneNumber;

    @OneToMany(mappedBy = "owner")
    private List<RestaurantEntity> restaurants = new ArrayList<>();

    public OwnerEntity(String loginId, String name, Password password, PhoneNumber phoneNumber) {
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}