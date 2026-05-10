import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./MyOrders.css";
import AuthService from "../../services/AuthService";

export default function MyOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();
  useEffect(() => {
    const controller = new AbortController();

    const fetchOrders = async () => {
      try {
        setLoading(true);
        setError("");

        const token = AuthService.getToken();

        if (!token) {
          throw new Error("No token found. Please login again.");
        }

        const response = await fetch("http://localhost:8081/api/v1.0/orders", {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
          signal: controller.signal,
          credentials: "include",
        });

        const text = await response.text();
        const data = text ? JSON.parse(text) : [];

        if (!response.ok) {
          throw new Error(
            data.message || data.error || "Failed to fetch orders",
          );
        }

        setOrders(Array.isArray(data) ? data : []);
      } catch (err) {
        if (err.name !== "AbortError") {
          setError(err.message || "Something went wrong");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();

    return () => controller.abort();
  }, []);

  return (
    <div className="my-orders-page">
      <div className="orders-header">
        <div>
          <h1 className="orders-title">My Orders</h1>
          <p className="orders-subtitle">
            View all the orders you placed from your account.
          </p>
        </div>

        <button className="back-button" onClick={() => navigate("/home")}>
          Back to Home
        </button>
      </div>

      {loading && <div className="orders-message">Loading your orders...</div>}
      {!loading && error && <div className="orders-message error">{error}</div>}
      {!loading && !error && orders.length === 0 && (
        <div className="orders-empty">No orders found.</div>
      )}

      {!loading && !error && orders.length > 0 && (
        <div className="orders-list">
          {orders.map((order) => (
            <div className="order-card" key={order.orderId}>
              <div className="order-card-header">
                <div>
                  <h2 className="order-id">Order #{order.orderId}</h2>
                  <p className="order-date">
                    {order.createdAt
                      ? new Date(order.createdAt).toLocaleString()
                      : "Date not available"}
                  </p>
                </div>

                <div className="order-badges">
                  <span
                    className={`badge order-status ${String(order.orderStatus || "").toLowerCase()}`}
                  >
                    {order.orderStatus}
                  </span>
                  <span
                    className={`badge payment-status ${String(order.paymentStatus || "").toLowerCase()}`}
                  >
                    {order.paymentStatus}
                  </span>
                </div>
              </div>

              <div className="shipping-block">
                <h3>Shipping Details</h3>
                <p>{order.fullName}</p>
                <p>{order.phone}</p>
                <p>
                  {order.addressLine1}
                  {order.addressLine2 ? `, ${order.addressLine2}` : ""}
                </p>
                <p>
                  {order.city}, {order.state} - {order.pincode}
                </p>
                <p>Payment Method: {order.paymentMethod}</p>
              </div>

              <div className="items-block">
                <h3>Ordered Items</h3>

                {order.items && order.items.length > 0 ? (
                  <div className="order-items">
                    {order.items.map((item) => (
                      <div className="order-item" key={item.orderItemId}>
                        <img
                          src={item.imageUrl}
                          alt={item.name}
                          className="item-image"
                        />
                        <div className="item-info">
                          <h4>{item.name}</h4>
                          <p>Product ID: {item.productId}</p>
                          <p>Price: ₹ {item.price}</p>
                          <p>Quantity: {item.quantity}</p>
                          <p>Subtotal: ₹ {item.subtotal}</p>
                        </div>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="no-items">No items found for this order.</p>
                )}
              </div>

              <div className="order-footer">
                <span>Total Amount</span>
                <strong>₹ {order.totalAmount}</strong>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
