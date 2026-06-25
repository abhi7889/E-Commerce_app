import React, { useState } from "react";
import { FaShoppingCart, FaSearch, FaChevronDown } from "react-icons/fa";
import "./HomeHeader.css";

export default function HomeHeader({
  onLogout,
  onCartClick,
  onProfileClick,
  onOrdersClick,
  onAdminDashboardClick,
  searchTerm,
  setSearchTerm,
  selectedCategory,
  setSelectedCategory,
  categories = [],
  userName = "Abhishek Sharma",
  cartCount = 0,
}) {
  const [showDropdown, setShowDropdown] = useState(false);

  const getInitials = (name) => {
    if (!name) return "U";
    return name
      .split(" ")
      .map((word) => word[0])
      .join("")
      .toUpperCase()
      .slice(0, 2);
  };
  console.log("HomeHeader userName:", userName);
  console.log("HomeHeader initials:", getInitials(userName));
  return (
    <header className="homepage-header">
      <div className="logo">TechStore</div>

      <div className="header-search">
        <FaSearch className="search-icon" />
        <input
          type="text"
          placeholder="Search products..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>

      <div className="header-actions">
        {setSelectedCategory && (
          <select
            className="category-select"
            value={selectedCategory}
            onChange={(e) => setSelectedCategory(e.target.value)}
          >
            {categories.map((category) => (
              <option key={category} value={category}>
                {category}
              </option>
            ))}
          </select>
        )}

        <button className="cart-button" onClick={onCartClick}>
          <FaShoppingCart />
          {cartCount > 0 && <span className="cart-badge">{cartCount}</span>}
        </button>

        <div className="user-menu">
          <button
            className="user-button"
            onClick={() => setShowDropdown((prev) => !prev)}
          >
            <span className="user-avatar">{getInitials(userName)}</span>

            <FaChevronDown className="dropdown-icon" />
          </button>

          {showDropdown && (
            <div className="dropdown-menu">
              <button
                onClick={() => {
                  setShowDropdown(false);
                  onProfileClick?.();
                }}
              >
                Profile
              </button>

              <button
                onClick={() => {
                  setShowDropdown(false);
                  onOrdersClick?.();
                }}
              >
                My Orders
              </button>

              <button
                onClick={() => {
                  setShowDropdown(false);
                  onAdminDashboardClick?.();
                }}
              >
                Admin Dashboard
              </button>

              <button
                onClick={() => {
                  setShowDropdown(false);
                  onLogout?.();
                }}
                className="logout-btn"
              >
                Logout
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
}
