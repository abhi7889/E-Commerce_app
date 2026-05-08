import React from "react";
import ProductCard from "../ProductCard/ProductCard";

export default function ProductList({
  products,
  loading,
  error,
  onCartUpdated,
}) {
  return (
    <section className="products-section">
      {loading && <p className="status-message">Loading products...</p>}
      {error && <p className="status-message error">{error}</p>}

      {!loading && !error && products.length === 0 && (
        <p className="status-message">No products available right now.</p>
      )}

      {!loading && !error && products.length > 0 && (
        <div className="products-grid">
          {products.map((product, index) => (
            <ProductCard
              key={product.productId || product.id || index}
              product={product}
              onCartUpdated={onCartUpdated}
            />
          ))}
        </div>
      )}
    </section>
  );
}
