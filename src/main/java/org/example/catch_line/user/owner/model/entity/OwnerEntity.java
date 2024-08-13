package org.example.catch_line.user.owner.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.example.catch_line.user.member.model.mapper.converter.PasswordConverter;
import org.example.catch_line.user.member.model.mapper.converter.PhoneNumberConverter;
import org.example.catch_line.user.member.model.vo.Password;
import org.example.catch_line.user.member.model.vo.PhoneNumber;

@Table(name = "owner")
@NoArgsConstructor
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


    @Builder
    public OwnerEntity(String loginId, String name, Password password, PhoneNumber phoneNumber) {
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}