package com.msws.shareplates;

import org.junit.jupiter.api.Test;


public class SimpleTest {
	
	@Test
	public void test() {
		
		String [] input = {"abc", "cde"};
		
		StringBuilder sb = new StringBuilder();
		String prefix = "";
		for(String each : input) {
			sb.append(prefix);
			prefix = ",";
			sb.append(each);
		}
		
		System.out.println(sb.toString());
		
	}

}
