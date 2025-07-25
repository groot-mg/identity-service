package com.generoso.identity.api.controller;

import com.generoso.identity.exception.error.ValidationErrorDetails;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.generoso.identity.utils.JsonUtils.fromJson;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class CommonControllerTest {

    static void missingFieldsAssert400Response(MockMvc mockMvc, String path, String jsonBody,
                                               String field, String fieldMessage) throws Exception {
        // Act
        var result = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        var responseBody = result.getResponse().getContentAsString();
        var errorDetails = fromJson(responseBody, ValidationErrorDetails.class);

        var fieldSplit = field.split(",");
        var fieldMessageSplit = fieldMessage.split(",");

        assertThat(errorDetails.getStatus()).isEqualTo(400);
        assertThat(errorDetails.getError()).isEqualTo("Field validation error");
        stream(fieldSplit).forEach(fieldValue -> assertThat(errorDetails.getField()).contains(fieldValue));
        stream(fieldMessageSplit).forEach(
                fieldMessageValue -> assertThat(errorDetails.getFieldMessage()).contains(fieldMessageValue));
        assertThat(errorDetails.getTimestamp()).isNotNull();
    }
}
