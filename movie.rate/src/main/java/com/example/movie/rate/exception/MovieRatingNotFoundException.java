package com.example.movie.rate.exception;

public class MovieRatingNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public MovieRatingNotFoundException(String message) {
		super(message);
	}

}
