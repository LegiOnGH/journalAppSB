package com.legion.journal.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepoImpTests {

    @Autowired
    private UserRepoImp userRepoImp;

    @Test
    public void testA(){
        userRepoImp.getUsersForSA();
    }
}
