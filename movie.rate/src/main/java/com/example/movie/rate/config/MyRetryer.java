package com.example.movie.rate.config;

import java.util.concurrent.TimeUnit;

import feign.RetryableException;
import feign.Retryer;

public class MyRetryer implements Retryer {
	@Override
	public void continueOrPropagate(RetryableException e) {
		throw e;
	}

	@Override
	public Retryer clone() {
		return new Default(2000, TimeUnit.SECONDS.toMillis(1), 5);
	}
}