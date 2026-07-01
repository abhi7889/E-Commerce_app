import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import AuthService from "../../services/AuthService";
import CartService from "../../services/CartService";
import "./ProductDetails.css";
import HomeHeader from "../HomeHeader/HomeHeader";

const API_BASE_URL = "https://e-commerceapp.up.railway.app/api/v1.0";

export default function ProductDetails({ onCartUpdated }) {
  const { productId } = useParams();
  const navigate = useNavigate();

  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [cartItems, setCartItems] = useState([]);

  useEffect(() => {
    fetch(`${API_BASE_URL}/products/${productId}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch product");
        }
        return response.json();
      })
      .then((data) => {
        setProduct(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setError("Unable to load product");
        setLoading(false);
      });
  }, [productId]);

  const loadCart = async () => {
    try {
      const data = await CartService.getCart();
      if (Array.isArray(data)) setCartItems(data);
      else if (Array.isArray(data.items)) setCartItems(data.items);
      else if (Array.isArray(data.cartItems)) setCartItems(data.cartItems);
      else setCartItems([]);
    } catch (err) {
      console.error("Failed to load cart", err);
      setCartItems([]);
    }
  };

  useEffect(() => {
    if (AuthService.isAuthenticated()) {
      loadCart();
    }
  }, []);

  const handleAddToCart = async () => {
    const token = AuthService.getToken?.();
    if (!token) {
      navigate("/login");
      return;
    }

    try {
      await CartService.addToCart(product.productId, 1);
      await loadCart();

      if (onCartUpdated) {
        onCartUpdated(`${product.name} added to cart`);
      }

      setMessage(`${product.name} added to cart`);
      setTimeout(() => setMessage(""), 2000);
    } catch (err) {
      console.error("Add to cart failed", err);
      setError("Failed to add to cart");
      setTimeout(() => setError(""), 2000);
    }
  };

  const cartCount = cartItems.reduce(
    (sum, item) => sum + (item.quantity || 1),
    0,
  );

  if (loading) {
    return <p style={{ color: "white", textAlign: "center" }}>Loading...</p>;
  }

  if (error && !product) {
    return <p style={{ color: "white", textAlign: "center" }}>{error}</p>;
  }

  if (!product) {
    return (
      <p style={{ color: "white", textAlign: "center" }}>Product not found</p>
    );
  }

  return (
    <>
      <HomeHeader
        onLogout={() => {
          AuthService.logout();
          navigate("/login");
        }}
        onCartClick={() => navigate("/cart")}
        onProfileClick={() => navigate("/profile")}
        onOrdersClick={() => navigate("/orders")}
        searchTerm=""
        setSearchTerm={() => {}}
        cartCount={cartCount}
        userName="Abhishek Sharma"
      />

      <div className="product-details-page">
        {message && <div className="cart-toast">{message}</div>}
        {error && product && (
          <div className="cart-toast error-toast">{error}</div>
        )}

        <div className="product-details-container">
          <img
            src={
              product.imageUrl || "https://placehold.co/400x400?text=No+Image"
            }
            alt={product.name}
            className="details-image"
          />

          <div className="details-info">
            <button className="back-btn" onClick={() => navigate(-1)}>
              <span className="back-icon">←</span>
              <span>Back</span>
            </button>

            <p className="details-category">{product.category}</p>
            <h1>{product.name}</h1>
            <p className="details-description">{product.description}</p>
            <p className="details-price">Price : ₹ {product.price}</p>

            <button className="add-cart-btn" onClick={handleAddToCart}>
              Add to Cart
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
