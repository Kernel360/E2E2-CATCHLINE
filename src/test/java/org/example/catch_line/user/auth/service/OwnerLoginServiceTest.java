package org.example.catch_line.user.auth.service;

import org.example.catch_line.user.auth.details.OwnerUserDetails;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.example.catch_line.user.owner.repository.OwnerRepository;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class OwnerLoginServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerLoginService ownerLoginService;

    private OwnerEntity ownerEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock된 OwnerEntity 생성
        ownerEntity = new OwnerEntity(
                "testOwner",
                "Test Owner",
                new Password("encryptedPassword"),
                new PhoneNumber("010-1234-5678")
        );
    }

    @Test
    void loadUserByUsername_OwnerExists() {
        // given
        when(ownerRepository.findByLoginId(anyString())).thenReturn(java.util.Optional.of(ownerEntity));

        // when
        OwnerUserDetails userDetails = (OwnerUserDetails) ownerLoginService.loadUserByUsername("testOwner");

        // then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("testOwner");
        assertThat(userDetails.getAuthorities()).isNotEmpty();
    }

    @Test
    void loadUserByUsername_OwnerDoesNotExist() {
        // given
        when(ownerRepository.findByLoginId(anyString())).thenReturn(java.util.Optional.empty());

        // when / then
        assertThatThrownBy(() -> ownerLoginService.loadUserByUsername("nonexistentOwner"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("는 존재하지 않는 식당 사장님입니다.");
    }
}
