package com.infy.backend.controller;

import org.springframework.web.bind.annotation.*;

import com.infy.backend.io.CreatePaymentIntentRequest;
import com.infy.backend.io.CreatePaymentIntentResponse;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @PostMapping("/create-intent")
    public CreatePaymentIntentResponse createPaymentIntent(
            @RequestBody CreatePaymentIntentRequest request) throws Exception {

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(request.getAmount())
                .setCurrency(request.getCurrency())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return new CreatePaymentIntentResponse(paymentIntent.getClientSecret());
    }
}