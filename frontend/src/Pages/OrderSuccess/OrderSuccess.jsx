import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./OrderSuccess.css";

export default function OrderSuccess() {
  const navigate = useNavigate();
  const location = useLocation();

  const order = location.state?.order;

  if (!order) {
    return (
      <div className="order-success-page">
        <div className="order-success-card">
          <h2>Order details not found</h2>
          <p>Please place an order first.</p>
          <button
            className="success-btn primary-btn"
            onClick={() => navigate("/home")}
          >
            Go to Home
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="order-success-page">
      <div className="order-success-card">
        <div className="success-icon">✅</div>
        <h1>Order Placed Successfully</h1>
        <p className="success-message">
          Thank you for your purchase. Your order has been placed successfully.
        </p>

        <div className="order-info-box">
          <div className="info-row">
            <span>Order ID</span>
            <strong>{order.orderId}</strong>
          </div>
          <div className="info-row">
            <span>Status</span>
            <strong>{order.orderStatus}</strong>
          </div>
          <div className="info-row">
            <span>Payment</span>
            <strong>{order.paymentMethod}</strong>
          </div>
          <div className="info-row">
            <span>Total Amount</span>
            <strong>₹ {order.totalAmount}</strong>
          </div>
        </div>

        <div className="delivery-box">
          <h3>Delivery Address</h3>
          <p>{order.fullName}</p>
          <p>{order.phone}</p>
          <p>{order.addressLine1}</p>
          {order.addressLine2 && <p>{order.addressLine2}</p>}
          <p>
            {order.city}, {order.state} - {order.pincode}
          </p>
        </div>

        <div className="success-actions">
          <button
            className="success-btn secondary-btn"
            onClick={() => navigate("/home")}
          >
            Continue Shopping
          </button>

          <button
            className="success-btn primary-btn"
            onClick={() => navigate("/myorders")}
          >
            View My Orders
          </button>
        </div>
      </div>
    </div>
  );
}
