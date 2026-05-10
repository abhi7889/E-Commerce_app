import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Elements } from "@stripe/react-stripe-js";
import { stripePromise } from "../../stripe";
import PaymentService from "../../services/PaymentService";
import CheckoutForm from "./CheckoutForm";
import "./PaymentPage.css";

export default function PaymentPage() {
  const location = useLocation();
  const navigate = useNavigate();

  const orderPayload = location.state?.orderPayload;
  const cartItems = location.state?.cartItems || [];
  const subtotal = location.state?.subtotal || 0;

  const [clientSecret, setClientSecret] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const createIntent = async () => {
      try {
        const amountInPaise = Math.round(Number(subtotal) * 100);
        const response =
          await PaymentService.createPaymentIntent(amountInPaise);
        setClientSecret(response.clientSecret);
      } catch (err) {
        console.error(err);
        setError("Failed to initialize payment");
      }
    };

    if (orderPayload && subtotal > 0) {
      createIntent();
    }
  }, [orderPayload, subtotal]);

  if (!orderPayload) {
    return (
      <div className="payment-page">
        <div className="payment-card">
          <h2>Payment session not found</h2>
          <button className="back-btn" onClick={() => navigate("/checkout")}>
            Go to Checkout
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="payment-page">
      <div className="payment-card">
        <h1>Online Payment</h1>
        <p>Complete your payment to confirm the order.</p>

        <div className="payment-summary">
          {cartItems.map((item) => (
            <div
              className="payment-item"
              key={item.cartItemId || item.productId}
            >
              <span>{item.name}</span>
              <span>
                {item.quantity} x ₹ {item.price}
              </span>
            </div>
          ))}

          <div className="payment-total">
            <strong>Total</strong>
            <strong>₹ {subtotal}</strong>
          </div>
        </div>

        {error && <p className="payment-error">{error}</p>}

        {clientSecret ? (
          <Elements stripe={stripePromise} options={{ clientSecret }}>
            <CheckoutForm orderPayload={orderPayload} navigate={navigate} />
          </Elements>
        ) : (
          <p>Loading payment...</p>
        )}
      </div>
    </div>
  );
}
