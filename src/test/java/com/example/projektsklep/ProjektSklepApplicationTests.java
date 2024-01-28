package com.example.projektsklep;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjektSklepApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void simpleTest(){
        int result =2+3;
        Assertions.assertEquals(5 , result);
    }


}
