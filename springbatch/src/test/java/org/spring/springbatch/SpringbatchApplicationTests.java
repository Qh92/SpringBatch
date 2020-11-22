package org.spring.springbatch;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class SpringbatchApplicationTests {

	@Test
	public void contextLoads() {
		
		
		StringUtils.isNotBlank("");
	}
	public static void main(String[] args) {
		System.out.println(StringUtils.isEmpty(" "));
	}
	

}
