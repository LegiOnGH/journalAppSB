package com.legion.journal.service;

import com.legion.journal.repository.UserRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepo userRepo;

    @Disabled("Eve hi")
    @Test
    void testFindByUserName(){
        assertEquals(4, 2+2);
        assertNotNull(userRepo.findByUserName("Rohit"));
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,4,6",
            "3,3,6"
    })
    void test(int a, int b, int expected){
        assertEquals(expected, a+b, "failed for: " + a +"+"+ b+"="+ expected);
    }
}
