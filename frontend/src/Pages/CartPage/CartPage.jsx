import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import CartService from "../../services/CartService";
import HomeHeader from "../../components/HomeHeader/HomeHeader";
import AuthService from "../../services/AuthService";
import "./CartPage.css";

export default function CartPage() {
  const navigate = useNavigate();

  const [cartData, setCartData] = useState(null);
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const loadCart = async () => {
    try {
      setLoading(true);
      const data = await CartService.getCart();

      setCartData(data);
      setCartItems(Array.isArray(data.items) ? data.items : []);
      setError("");
    } catch (err) {
      console.error("Failed to load cart:", err);
      setError("Unable to load cart");
      setCartItems([]);
      setCartData(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCart();
  }, []);

  const handleRemove = async (cartItemId) => {
    try {
      await CartService.removeCartItem(cartItemId);
      setMessage("Item removed from cart");
      await loadCart();
      setTimeout(() => setMessage(""), 2000);
    } catch (err) {
      console.error("Remove failed:", err);
      setError("Failed to remove item");
      setTimeout(() => setError(""), 2000);
    }
  };

  const handleQuantityChange = async (cartItemId, newQuantity) => {
    try {
      if (newQuantity < 1) {
        await handleRemove(cartItemId);
        return;
      }

      await CartService.updateCartItem(cartItemId, newQuantity);
      setMessage("Cart updated");
      await loadCart();
      setTimeout(() => setMessage(""), 2000);
    } catch (err) {
      console.error("Quantity update failed:", err);
      setError("Failed to update quantity");
      setTimeout(() => setError(""), 2000);
    }
  };

  const handleLogout = () => {
    AuthService.logout();
    navigate("/login");
  };

  const cartCount = cartItems.reduce(
    (sum, item) => sum + (item.quantity || 0),
    0,
  );

  const subtotal =
    cartData?.totalAmount ??
    cartItems.reduce((sum, item) => sum + (item.subtotal || 0), 0);

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

      <div className="cart-page">
        <div className="cart-topbar">
          <button className="back-btn" onClick={() => navigate("/home")}>
            ← Back to shopping
          </button>
        </div>

        <h2 className="cart-title">Your Cart</h2>

        {message && <div className="cart-toast">{message}</div>}
        {error && <div className="cart-toast error-toast">{error}</div>}

        {loading ? (
          <p className="empty-cart">Loading cart...</p>
        ) : cartItems.length === 0 ? (
          <p className="empty-cart">Your cart is empty.</p>
        ) : (
          <div className="cart-layout">
            <div className="cart-items">
              {cartItems.map((item) => (
                <div className="cart-card" key={item.cartItemId}>
                  <img
                    src={
                      item.imageUrl ||
                      "https://placehold.co/100x100?text=No+Image"
                    }
                    alt={item.name || "Product"}
                    className="cart-img"
                  />

                  <div className="cart-info">
                    <h3>{item.name}</h3>
                    <p className="cart-price">₹ {item.price}</p>

                    <div className="qty-box">
                      <button
                        onClick={() =>
                          handleQuantityChange(
                            item.cartItemId,
                            item.quantity - 1,
                          )
                        }
                      >
                        -
                      </button>
                      <span>{item.quantity}</span>
                      <button
                        onClick={() =>
                          handleQuantityChange(
                            item.cartItemId,
                            item.quantity + 1,
                          )
                        }
                      >
                        +
                      </button>
                    </div>
                  </div>

                  <div className="cart-actions">
                    <p className="line-total">₹ {item.subtotal}</p>
                    <button onClick={() => handleRemove(item.cartItemId)}>
                      Remove
                    </button>
                  </div>
                </div>
              ))}
            </div>

            <div className="cart-summary">
              <h3>Order Summary</h3>
              <div className="summary-row">
                <span>Total Items</span>
                <span>{cartCount}</span>
              </div>
              <div className="summary-row">
                <span>Subtotal</span>
                <strong>₹ {subtotal}</strong>
              </div>
              <button
                className="checkout-btn"
                onClick={() => navigate("/checkout")}
              >
                Proceed to Checkout
              </button>
            </div>
          </div>
        )}
      </div>
    </>
  );
}
