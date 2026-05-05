import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import './HomePage.css';

export default function HomePage() {
  const navigate = useNavigate();

  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const handleLogout = () => {
    AuthService.logout();
    navigate('/login');
  };

  useEffect(() => {
    fetch('http://localhost:8081/api/v1.0/products')
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to fetch products');
        }
        return response.json();
      })
      .then((data) => {
        console.log('Products API response:', data);

        if (Array.isArray(data)) {
          setProducts(data);
        } else {
          setProducts([]);
          setError('Invalid product response format');
        }

        setLoading(false);
      })
      .catch(() => {
        setError('Unable to load products');
        setLoading(false);
      });
  }, []);

  return (
    <div className="homepage-container">
      <header className="homepage-header">
        <div className="logo">E-Commerce</div>
        <button className="logout-button" onClick={handleLogout}>
          Logout
        </button>
      </header>

      <section className="hero-section">
        <h1>Shop the best products for your lifestyle</h1>
        <p>Browse top-selling items and choose what fits your needs.</p>
      </section>

      <section className="products-section">
        <h2>Featured Products</h2>

        {loading && <p className="status-message">Loading products...</p>}
        {error && <p className="status-message error">{error}</p>}

        {!loading && !error && products.length === 0 && (
          <p className="status-message">No products available right now.</p>
        )}

        {!loading && !error && products.length > 0 && (
          <div className="products-grid">
            {products.map((product, index) => (
              <div
                className="product-card"
                key={product.productId || product.id || index}
              >
               <img
                  src={product.imageUrl }
                  alt={product.name}
                  className="product-image"
                />
                <div className="product-info">
                  <h3>{product.name || 'No Name'}</h3>
                  <p className="product-category">{product.category || 'No Category'}</p>
                  <p className="product-description">
                    {product.description || 'No description available'}
                  </p>
                  <p className="product-price">₹ {product.price ?? 'N/A'}</p>
                  <button className="buy-button">View Product</button>
                </div>
              </div>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}