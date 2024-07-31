package org.example.catch_line.member.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.catch_line.common.BaseTimeEntity;
import org.example.catch_line.common.constant.Role;

@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone_number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}
