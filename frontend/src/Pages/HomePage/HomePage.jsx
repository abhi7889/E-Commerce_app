import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthService from "../../services/AuthService";
import "./HomePage.css";
import HomeHeader from "../../components/HomeHeader/HomeHeader";
import HeroSection from "../../components/HeroSection/HeroSection";
import ProductList from "../../components/ProductList/ProductList";

export default function HomePage() {
  const navigate = useNavigate();

  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [cartMessage, setCartMessage] = useState("");

  const handleLogout = () => {
    AuthService.logout();
    navigate("/login");
  };

  useEffect(() => {
    fetch("http://localhost:8081/api/v1.0/products")
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch products");
        }
        return response.json();
      })
      .then((data) => {
        if (Array.isArray(data)) {
          setProducts(data);
        } else {
          setProducts([]);
          setError("Invalid product response format");
        }
        setLoading(false);
      })
      .catch(() => {
        setError("Unable to load products");
        setLoading(false);
      });
  }, []);

  const handleCartUpdated = (message) => {
    setCartMessage(message);
    setTimeout(() => {
      setCartMessage("");
    }, 2000);
  };

  return (
    <div className="homepage-container">
      <HomeHeader onLogout={handleLogout} />
      <HeroSection />

      {cartMessage && <div className="cart-toast">{cartMessage}</div>}

      <ProductList
        products={products}
        loading={loading}
        error={error}
        onCartUpdated={handleCartUpdated}
      />
    </div>
  );
}
