package com.example.dataformat.validation;

import com.example.dataformat.common.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
@Import(GlobalExceptionHandler.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Успешная регистрация — 201 Created
    @Test
    void register_validRequest_returns201WithLocation() throws Exception {
        String body = """
                {
                  "name": "Alice",
                  "email": "alice@example.com",
                  "password": "secret123",
                  "age": 25
                }
                """;

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", startsWith("/api/users/")))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    // Ошибка валидации — 422 + violations
    @Test
    void register_invalidEmail_returns422WithViolation() throws Exception {
        String body = """
                {
                  "name": "Alice",
                  "email": "not-an-email",
                  "password": "secret123",
                  "age": 25
                }
                """;

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.instance").value("/api/registration"))
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("email"))
                .andExpect(jsonPath("$.violations[0].code").value("Email"))
                .andExpect(jsonPath("$.violations[0].message").value("email must be valid"));
    }

    @Test
    void register_allInvalidFields_returns422WithMultipleViolations() throws Exception {
        String body = """
                {
                  "name": "",
                  "email": "not-an-email",
                  "password": "123",
                  "age": 15
                }
                """;

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.violations", hasSize(greaterThanOrEqualTo(5))))
                .andExpect(jsonPath("$.violations[*].field",
                        hasItems("name", "email", "password", "age")))
                .andExpect(jsonPath("$.violations[*].code",
                        hasItems("NotBlank", "Email", "Size", "Pattern", "Min")));
    }

    //Сломанный JSON — 400
    @Test
    void register_malformedJson_returns400() throws Exception {
        String body = """
                {
                  "name": "Alice",
                  "email":
                }
                """;

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.title").value("Invalid JSON"))
                .andExpect(jsonPath("$.code").value("JSON_INVALID"))
                .andExpect(jsonPath("$.detail").exists());
    }

    //Лишнее поле при строгой десериализации — 400
    @Test
    void register_unknownField_returns400() throws Exception {
        String body = """
                {
                  "name": "Alice",
                  "email": "alice@example.com",
                  "password": "secret123",
                  "age": 25,
                  "isAdmin": true
                }
                """;

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.title").value("Invalid JSON"))
                .andExpect(jsonPath("$.code").value("JSON_INVALID"))
                .andExpect(jsonPath("$.detail", containsString("isAdmin")));
    }

    //Неподдерживаемый Content-Type — 415
    @Test
    void register_unsupportedContentType_returns415() throws Exception {
        String body = """
                <customer>
                     <name>Alice</name>
                     <email>alice@example.com</email>
                     <password>secret123</password>
                     <age>25</age>
                 </customer>
                """;
        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_XML)
                        .content(body))
                .andExpect(status().isUnsupportedMediaType());
    }

    // Неподдерживаемый Accept — 406
    @Test
    void register_unsupportedAccept_returns406() throws Exception {
        String body = """
                {
                  "name": "Alice",
                  "email": "alice@example.com",
                  "password": "secret123",
                  "age": 25
                }
                """;

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_XML)
                        .content(body))
                .andExpect(status().isNotAcceptable());
    }

    //Все поля ProblemDetail присутствуют
    @Test
    void register_errorResponse_containsAllProblemDetailFields() throws Exception {
        String body = """
                {
                  "name": "Alice",
                  "email": "bad",
                  "password": "secret123",
                  "age": 25
                }
                """;

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").isString())
                .andExpect(jsonPath("$.status").isNumber())
                .andExpect(jsonPath("$.detail").isString())
                .andExpect(jsonPath("$.instance").isString())
                .andExpect(jsonPath("$.violations").isArray());
    }
}