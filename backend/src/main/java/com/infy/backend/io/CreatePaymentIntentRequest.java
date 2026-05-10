package com.infy.backend.io;

import lombok.Data;

@Data
public class CreatePaymentIntentRequest {
    private Long amount;
    private String currency;
}