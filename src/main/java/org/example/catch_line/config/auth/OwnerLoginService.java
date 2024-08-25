package org.example.catch_line.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.exception.login.LoginException;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.example.catch_line.user.owner.repository.OwnerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerLoginService implements UserDetailsService {

    private final OwnerRepository ownerRepository;
    private OwnerEntity owner;

    @Override
    public UserDetails loadUserByUsername(String username) throws LoginException {

        log.info("사장님 로그인 서비스 시작");
        OwnerEntity owner = ownerRepository.findByLoginId(username).orElseThrow(()-> new LoginException("존재하지 않는 사장님"));
        return new OwnerUserDetails(owner);

    }
}
