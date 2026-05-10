import React, { useState } from "react";
import {
  PaymentElement,
  useElements,
  useStripe,
} from "@stripe/react-stripe-js";
import OrderService from "../../services/OrderService";

export default function CheckoutForm({ orderPayload, navigate }) {
  const stripe = useStripe();
  const elements = useElements();
  const [processing, setProcessing] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!stripe || !elements) return;

    setProcessing(true);
    setErrorMessage("");

    const { error: submitError } = await elements.submit();
    if (submitError) {
      setErrorMessage(submitError.message || "Invalid payment details");
      setProcessing(false);
      return;
    }

    const result = await stripe.confirmPayment({
      elements,
      redirect: "if_required",
    });

    if (result.error) {
      setErrorMessage(result.error.message || "Payment failed");
      setProcessing(false);
      return;
    }

    try {
      const response = await OrderService.placeOrder({
        ...orderPayload,
        paymentMethod: "ONLINE",
      });

      navigate("/order-success", {
        state: { order: response },
      });
    } catch (err) {
      setErrorMessage(err.message || "Order creation failed after payment");
    } finally {
      setProcessing(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="stripe-form">
      <PaymentElement />

      {errorMessage && <p className="payment-error">{errorMessage}</p>}

      <button
        className="pay-btn"
        type="submit"
        disabled={!stripe || processing}
      >
        {processing ? "Processing Payment..." : "Pay Now"}
      </button>
    </form>
  );
}
