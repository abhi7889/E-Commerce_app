import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import AuthService from "../../services/AuthService";
import CartService from "../../services/CartService";
import "./ProductDetails.css";
import HomeHeader from "../HomeHeader/HomeHeader";

const API_BASE_URL = "http://localhost:8081/api/v1.0";

export default function ProductDetails() {
  const { productId } = useParams();
  const navigate = useNavigate();

  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

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

  const handleAddToCart = async () => {
    const token = AuthService.getToken?.();

    if (!token) {
      navigate("/login");
      return;
    }

    try {
      await CartService.addToCart(product.productId, 1);
      setMessage(`${product.name} added to cart`);
      setTimeout(() => setMessage(""), 2000);
    } catch (err) {
      console.error("Add to cart failed", err);
      setError("Failed to add to cart");
      setTimeout(() => setError(""), 2000);
    }
  };

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
      <HomeHeader />
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
            <p className="details-price">₹ {product.price}</p>

            <button className="add-cart-btn" onClick={handleAddToCart}>
              Add to Cart
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
