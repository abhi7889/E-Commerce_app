import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthService from "../../services/AuthService";
import HomeHeader from "../../components/HomeHeader/HomeHeader";
import "./Profile.css";

const API_BASE_URL = "http://localhost:8081/api/v1.0";

export default function Profile() {
  const navigate = useNavigate();
  const token = AuthService.getToken();
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const isAdmin = user?.role === "ADMIN";

  const [profile, setProfile] = useState({
    name: "",
    email: "",
  });

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await fetch(`${API_BASE_URL}/profile`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        console.log("Profile response status:", response.status);

        const data = await response.json();
        console.log("Profile response data:", data);
        console.log("User name from API:", data.name);

        if (response.status === 401) {
          navigate("/login");
          return;
        }

        if (!response.ok) {
          throw new Error(data.message || "Failed to fetch profile");
        }

        setProfile({
          name: data.name || "",
          email: data.email || "",
        });

        const existingUser = JSON.parse(localStorage.getItem("user") || "{}");
        localStorage.setItem(
          "user",
          JSON.stringify({
            ...existingUser,
            name: data.name || existingUser.name || "User",
          }),
        );
      } catch (err) {
        console.error("Profile fetch error:", err);
        setError(err.message || "Failed to fetch profile");
      } finally {
        setLoading(false);
      }
    };

    if (!token) {
      navigate("/login");
      return;
    }

    fetchProfile();
  }, [navigate, token]);

  const handleLogout = () => {
    AuthService.logout();
    navigate("/login");
  };

  const getInitials = (name) => {
    if (!name) return "U";
    return name
      .split(" ")
      .map((word) => word[0])
      .join("")
      .toUpperCase()
      .slice(0, 2);
  };

  if (loading) {
    return (
      <div className="profile-loading-wrap">
        <div className="profile-loading-card">Loading profile...</div>
      </div>
    );
  }

  return (
    <div className="profile-layout">
      <HomeHeader
        onLogout={handleLogout}
        onCartClick={() => navigate("/cart")}
        onProfileClick={() => navigate("/profile")}
        onOrdersClick={() => navigate("/myorders")}
        onAdminDashboardClick={() => navigate("/admin/products")}
        searchTerm=""
        setSearchTerm={() => {}}
        selectedCategory="All"
        setSelectedCategory={() => {}}
        categories={["All"]}
        userName={profile?.name || user?.name || "User"}
        cartCount={0}
        isAdmin={isAdmin}
      />

      <div className="profile-page">
        <div className="profile-shell">
          <div className="profile-banner">
            <div className="profile-avatar">{getInitials(profile.name)}</div>
            <div className="profile-banner-text">
              <h1>My Profile</h1>
              <p>
                View your account details and manage your shopping activity.
              </p>
            </div>
          </div>

          <div className="profile-card">
            {error && <p className="error-message">{error}</p>}

            <div className="profile-section-title">Account Information</div>

            <div className="profile-field">
              <label>Full Name</label>
              <input type="text" value={profile.name} readOnly />
            </div>

            <div className="profile-field">
              <label>Email Address</label>
              <input type="email" value={profile.email} readOnly />
            </div>

            <div className="profile-meta">
              <div className="meta-box">
                <span className="meta-label">Account Type</span>
                <strong>{isAdmin ? "Admin" : "Customer"}</strong>
              </div>
              <div className="meta-box">
                <span className="meta-label">Status</span>
                <strong>Active</strong>
              </div>
            </div>

            <div className="profile-actions">
              <button
                className="secondary-btn"
                onClick={() => navigate("/myorders")}
              >
                My Orders
              </button>
              <button className="ghost-btn" onClick={() => navigate("/home")}>
                Back to Home
              </button>
              <button className="primary-btn" onClick={handleLogout}>
                Logout
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
