package com.example.v1.service;

/** Thrown when a request breaks a rule of the app. Mapped to HTTP 400. */
public class BadRequestException extends RuntimeException {

	public BadRequestException(String message) {
		super(message);
	}
}
