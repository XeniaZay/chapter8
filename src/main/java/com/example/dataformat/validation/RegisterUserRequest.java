package com.example.dataformat.validation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(@NotBlank(message = "name must not be blank")
                                  @Size(min = 2, max = 100, message = "name must be 2-100 characters")
                                  String name,

                                  @NotBlank(message = "email must not be blank")
                                  @Email(message = "email must be valid")
                                  @Size(max = 255, message = "email must not exceed 255 characters")
                                  String email,

                                  @NotBlank(message = "password must not be blank")
                                  @Size(min = 8, max = 100, message = "password must be 8-100 characters")
                                  @Pattern(
                                          regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                                          message = "password must contain at least one letter and one digit"
                                  )
                                  String password,

                                  @NotNull(message = "age must not be null")
                                  @Min(value = 18, message = "age must be at least 18")
                                  @Max(value = 120, message = "age must not exceed 120")
                                  Integer age) {
}
