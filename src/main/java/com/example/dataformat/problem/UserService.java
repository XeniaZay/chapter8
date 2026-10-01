package com.example.dataformat.problem;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    private static final Map<String, UserResponse> USERS = Map.of(
            "1", new UserResponse("1", "Alice"),
            "2", new UserResponse("2", "Bob"),
            "3", new UserResponse("2", "Charlie")
    );

    public UserResponse findById(String id) {
        UserResponse user = USERS.get(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }
}
