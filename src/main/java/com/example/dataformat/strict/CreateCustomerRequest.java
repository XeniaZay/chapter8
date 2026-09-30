package com.example.dataformat.strict;

import jakarta.validation.constraints.*;

public record CreateCustomerRequest(@NotBlank(message = "name is required")
                                    @Size(min = 2, max = 100, message = "name must be 2-100 characters")
                                    String name,

                                    @NotBlank(message = "email is required")
                                    @Email(message = "email must be valid")
                                    String email,

                                    @Min(value = 18, message = "age must be at least 18")
                                    @Max(value = 120, message = "age must not exceed 120")
                                    int age) {
}
