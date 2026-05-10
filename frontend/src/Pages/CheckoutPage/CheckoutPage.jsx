import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import CartService from "../../services/CartService";
import OrderService from "../../services/OrderService";
import HomeHeader from "../../components/HomeHeader/HomeHeader";
import AuthService from "../../services/AuthService";
import "./CheckoutPage.css";

export default function CheckoutPage() {
  const navigate = useNavigate();

  const [cartData, setCartData] = useState(null);
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [placingOrder, setPlacingOrder] = useState(false);
  const [error, setError] = useState("");
  const [formData, setFormData] = useState({
    fullName: "",
    phone: "",
    addressLine1: "",
    addressLine2: "",
    city: "",
    state: "",
    pincode: "",
    paymentMethod: "COD",
  });

  const loadCart = async () => {
    try {
      setLoading(true);
      const data = await CartService.getCart();
      setCartData(data);
      setCartItems(Array.isArray(data.items) ? data.items : []);
      setError("");
    } catch (err) {
      console.error("Failed to load cart:", err);
      setError("Unable to load checkout details");
      setCartData(null);
      setCartItems([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCart();
  }, []);

  const handleLogout = () => {
    AuthService.logout();
    navigate("/login");
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const validateForm = () => {
    if (
      !formData.fullName.trim() ||
      !formData.phone.trim() ||
      !formData.addressLine1.trim() ||
      !formData.city.trim() ||
      !formData.state.trim() ||
      !formData.pincode.trim()
    ) {
      setError("Please fill all required fields");
      return false;
    }
    return true;
  };

  const handlePlaceOrder = async () => {
    if (!validateForm()) return;

    const orderPayload = {
      fullName: formData.fullName,
      phone: formData.phone,
      addressLine1: formData.addressLine1,
      addressLine2: formData.addressLine2,
      city: formData.city,
      state: formData.state,
      pincode: formData.pincode,
      paymentMethod: formData.paymentMethod,
    };

    try {
      setPlacingOrder(true);
      setError("");

      if (formData.paymentMethod === "ONLINE") {
        navigate("/payment", {
          state: {
            orderPayload,
            cartItems,
            subtotal,
            cartCount,
          },
        });
        return;
      }

      const response = await OrderService.placeOrder(orderPayload);

      navigate("/order-success", {
        state: {
          order: response,
        },
      });
    } catch (err) {
      console.error("Place order failed:", err);
      setError(err.message || "Failed to place order");
    } finally {
      setPlacingOrder(false);
    }
  };

  const cartCount = cartItems.reduce(
    (sum, item) => sum + (item.quantity || 0),
    0,
  );

  const subtotal =
    cartData?.totalAmount ??
    cartItems.reduce((sum, item) => sum + Number(item.subtotal || 0), 0);

  return (
    <>
      <HomeHeader
        onLogout={handleLogout}
        onCartClick={() => navigate("/cart")}
        onProfileClick={() => navigate("/profile")}
        onOrdersClick={() => navigate("/orders")}
        searchTerm=""
        setSearchTerm={() => {}}
        cartCount={cartCount}
        userName="Abhishek Sharma"
      />

      <div className="checkout-page">
        <div className="checkout-topbar">
          <button className="back-btn" onClick={() => navigate("/cart")}>
            ← Back to cart
          </button>
        </div>

        <h2 className="checkout-title">Checkout</h2>

        {error && <div className="checkout-alert error-alert">{error}</div>}

        {loading ? (
          <p className="checkout-loading">Loading checkout...</p>
        ) : cartItems.length === 0 ? (
          <p className="checkout-loading">Your cart is empty.</p>
        ) : (
          <div className="checkout-layout">
            <div className="checkout-form-card">
              <h3>Delivery Details</h3>

              <div className="checkout-form-grid">
                <input
                  type="text"
                  name="fullName"
                  placeholder="Full Name"
                  value={formData.fullName}
                  onChange={handleChange}
                />
                <input
                  type="text"
                  name="phone"
                  placeholder="Phone Number"
                  value={formData.phone}
                  onChange={handleChange}
                />
                <input
                  type="text"
                  name="addressLine1"
                  placeholder="Address Line 1"
                  value={formData.addressLine1}
                  onChange={handleChange}
                />
                <input
                  type="text"
                  name="addressLine2"
                  placeholder="Address Line 2"
                  value={formData.addressLine2}
                  onChange={handleChange}
                />
                <input
                  type="text"
                  name="city"
                  placeholder="City"
                  value={formData.city}
                  onChange={handleChange}
                />
                <input
                  type="text"
                  name="state"
                  placeholder="State"
                  value={formData.state}
                  onChange={handleChange}
                />
                <input
                  type="text"
                  name="pincode"
                  placeholder="Pincode"
                  value={formData.pincode}
                  onChange={handleChange}
                />
              </div>

              <div className="payment-method-box">
                <h4>Payment Method</h4>

                <label>
                  <input
                    type="radio"
                    name="paymentMethod"
                    value="COD"
                    checked={formData.paymentMethod === "COD"}
                    onChange={handleChange}
                  />
                  Cash on Delivery
                </label>

                <label>
                  <input
                    type="radio"
                    name="paymentMethod"
                    value="ONLINE"
                    checked={formData.paymentMethod === "ONLINE"}
                    onChange={handleChange}
                  />
                  Online Payment
                </label>
              </div>
            </div>

            <div className="checkout-summary-card">
              <h3>Order Summary</h3>

              <div className="checkout-items">
                {cartItems.map((item) => (
                  <div
                    className="checkout-item"
                    key={item.cartItemId || item.productId}
                  >
                    <img
                      src={item.imageUrl}
                      alt={item.name}
                      className="checkout-item-img"
                    />
                    <div className="checkout-item-info">
                      <p>{item.name}</p>
                      <span>
                        {item.quantity} x ₹ {item.price}
                      </span>
                    </div>
                    <strong>₹ {item.subtotal}</strong>
                  </div>
                ))}
              </div>

              <div className="summary-row">
                <span>Total Items</span>
                <span>{cartCount}</span>
              </div>

              <div className="summary-row">
                <span>Subtotal</span>
                <strong>₹ {subtotal}</strong>
              </div>

              <button
                className="place-order-btn"
                onClick={handlePlaceOrder}
                disabled={placingOrder}
              >
                {placingOrder
                  ? "Processing..."
                  : formData.paymentMethod === "ONLINE"
                    ? "Proceed to Payment"
                    : "Place Order"}
              </button>
            </div>
          </div>
        )}
      </div>
    </>
  );
}
