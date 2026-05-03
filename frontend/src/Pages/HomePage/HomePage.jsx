import React from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';



export default function HomePage() {
  const navigate = useNavigate();

  const handleLogout = () => {
    navigate('/login');
  };

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

    </div>
  );
}
