package com.learning.avro_basics.specific;

import java.util.ArrayList;
import java.util.List;

public class NMissingNumbers {

	public static void main(String[] args) {
		
		int[] numbers = {4,2,1,7,8};
		int missingCount = 3;
		
		System.out.println(findNMissingNumber(numbers, missingCount));
	}
	
	//1 2 4 6 7 8
	public static List<Integer> findNMissingNumber(int[] numbers, int missingCount){
		boolean[] existNumbers = new boolean[numbers.length+missingCount+1];
		
		for(int number : numbers) {
			existNumbers[number] = true;
		}
		//why to grow elements
		List<Integer> missingNumbers = new ArrayList<>(missingCount);
		
		for(int number = 1; number < existNumbers.length ; number++) {
			if(!existNumbers[number])
				missingNumbers.add(number);
		}
		
		return missingNumbers;
		
	}
}
