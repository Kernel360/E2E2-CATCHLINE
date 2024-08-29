package org.example.catch_line.common.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HomeControllerTest {

    @InjectMocks
    HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("home 호출 테스트")
    @Test
    void homeTest(){
        String actualResponse = homeController.home();
        assertNotNull(actualResponse);
        assertEquals("redirect:/restaurants", actualResponse);
    }
}
