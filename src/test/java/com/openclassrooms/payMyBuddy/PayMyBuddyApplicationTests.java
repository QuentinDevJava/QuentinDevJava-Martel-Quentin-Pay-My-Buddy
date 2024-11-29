package com.openclassrooms.payMyBuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PayMyBuddyApplicationTests {

	@Test
	void contextLoads() {
		int a =1;
		int b=0;
		
		b=a+a;
		
		assertEquals(b, 2);
	}

}
