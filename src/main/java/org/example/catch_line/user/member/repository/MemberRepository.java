package org.example.catch_line.user.member.repository;

import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.common.model.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // 탈퇴한 회원을 제외하고, 동일한 이메일을 가진 회원이 존재하는지 탐색
    Optional<MemberEntity> findByEmailAndIsMemberDeletedFalse(Email email);

    // 탈퇴한 회원 제외하고, 회원이 존재하는지 탐색
    Optional<MemberEntity> findByMemberIdAndIsMemberDeletedFalse(Long memberId);

}
