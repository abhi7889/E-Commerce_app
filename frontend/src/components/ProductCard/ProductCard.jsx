import React from "react";
import { useNavigate } from "react-router-dom";
import "./ProductCard.css";
import cartIcon from "../../assets/images/icons8-cart-24.png";
import AuthService from "../../services/AuthService";
import CartService from "../../services/CartService";

export default function ProductCard({ product, onCartUpdated }) {
  const navigate = useNavigate();

  const handleOpenProduct = () => {
    navigate(`/products/${product.productId}`);
  };

  const handleAddToCart = async (e) => {
    e.stopPropagation();

    const token = AuthService.getToken?.();
    if (!token) {
      navigate("/login");
      return;
    }

    try {
      await CartService.addToCart(product.productId, 1);
      if (onCartUpdated) {
        onCartUpdated(`${product.name} added to cart`);
      }
    } catch (error) {
      console.error("Add to cart failed", error);
    }
  };

  return (
    <div className="productCard" onClick={handleOpenProduct}>
      <div className="product_image_div">
        <img
          loading="lazy"
          src={product.imageUrl || "https://placehold.co/300x300?text=No+Image"}
          alt={product.name}
          className="productImage"
        />

        <img
          onClick={handleAddToCart}
          className="cartIcon"
          src={cartIcon}
          alt="Add to cart"
        />
      </div>

      <div className="product_desc">
        <h2 className="productTitle">{product.name}</h2>
        <p className="productPrice">Price : ₹ {product.price}</p>
        <span>{product?.color}</span>
        <span>{product?.type}</span>
      </div>
    </div>
  );
}
