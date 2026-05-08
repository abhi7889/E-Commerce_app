import React from "react";

export default function ProductModal({ product, onClose, onAddToCart }) {
  return (
    <div className="product-modal-overlay" onClick={onClose}>
      <div className="product-modal" onClick={(e) => e.stopPropagation()}>
        <button className="modal-close-button" onClick={onClose}>
          ×
        </button>

        <div className="product-modal-content">
          <img
            src={
              product.imageUrl || "https://placehold.co/400x400?text=No+Image"
            }
            alt={product.name || "Product"}
            className="product-modal-image"
          />

          <div className="product-modal-info">
            <p className="modal-category">
              {product.category || "No Category"}
            </p>
            <h2>{product.name || "No Name"}</h2>
            <p className="modal-description">
              {product.description || "No description available"}
            </p>
            <p className="modal-price">₹ {product.price ?? "N/A"}</p>

            <div className="modal-actions">
              <button
                className="cart-button large"
                onClick={() => onAddToCart(product)}
              >
                Add to Cart
              </button>

              <button className="view-button secondary" onClick={onClose}>
                Continue Shopping
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
