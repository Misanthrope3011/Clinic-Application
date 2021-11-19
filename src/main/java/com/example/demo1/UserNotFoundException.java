package com.example.demo1;

public class UserNotFoundException extends RuntimeException {

        public UserNotFoundException(Long id) {
            super("Could not find User " + id);
        }
    public UserNotFoundException() {
        super("Could not find User ");
    }

}
