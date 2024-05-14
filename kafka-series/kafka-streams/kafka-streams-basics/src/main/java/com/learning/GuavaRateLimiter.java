package com.learning;

import com.google.common.util.concurrent.RateLimiter;

public class GuavaRateLimiter {

	public static void main(String[] args) {
		RateLimiter rateLimiter = RateLimiter.create(2);
		for(int i = 0 ; i < 10 ; i++) {
			rateLimiter.acquire(1);
			task();
		}
	}
	
	private static void task() {
		System.out.println("doing task by thread "+Thread.currentThread().getName());
	}

}
