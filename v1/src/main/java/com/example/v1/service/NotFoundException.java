package com.example.v1.service;

/** Thrown when an id does not exist. Mapped to HTTP 404. */
public class NotFoundException extends RuntimeException {

	public NotFoundException(String entity, Long id) {
		super(entity + " " + id + " not found");
	}
}
