package com.infy.backend.io;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePaymentIntentResponse {
    private String clientSecret;
}