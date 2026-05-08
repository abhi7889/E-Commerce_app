import React from "react";
import "./HomeHeader.css";

export default function HomeHeader({ onLogout }) {
  return (
    <header className="homepage-header">
      <div className="logo">E-Commerce</div>
      <button className="logout-button" onClick={onLogout}>
        Logout
      </button>
    </header>
  );
}
