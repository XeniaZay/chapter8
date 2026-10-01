package com.example.dataformat.problem;

public class UserNotFoundException extends RuntimeException {

    private final String userId;

    public UserNotFoundException(String userId) {
        super("User with id " + userId + " does not exist");
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
