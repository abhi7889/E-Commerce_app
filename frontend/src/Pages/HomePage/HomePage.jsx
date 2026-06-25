import React, { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthService from "../../services/AuthService";
import ProductService from "../../services/ProductService";
import CartService from "../../services/CartService";
import "./HomePage.css";
import HomeHeader from "../../components/HomeHeader/HomeHeader";
import HeroSection from "../../components/HeroSection/HeroSection";
import ProductList from "../../components/ProductList/ProductList";
import Footer from "../../components/Footer/Footer";

const API_BASE_URL = "http://localhost:8081/api/v1.0";

export default function HomePage() {
  const navigate = useNavigate();

  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const isAdmin = user?.role === "ADMIN";

  const [displayName, setDisplayName] = useState(user?.name || "User");
  const [products, setProducts] = useState([]);
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [cartMessage, setCartMessage] = useState("");
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("All");

  const handleLogout = () => {
    AuthService.logout();
    navigate("/login");
  };

  const loadProducts = async () => {
    try {
      const data = await ProductService.getAllProducts();
      if (Array.isArray(data)) {
        setProducts(data);
      } else if (Array.isArray(data.content)) {
        setProducts(data.content);
      } else {
        setProducts([]);
        setError("Invalid product response format");
      }
    } catch (err) {
      setError(err.message || "Unable to load products");
    }
  };

  const loadCart = async () => {
    try {
      const data = await CartService.getCart();
      if (Array.isArray(data)) setCartItems(data);
      else if (Array.isArray(data.items)) setCartItems(data.items);
      else if (Array.isArray(data.cartItems)) setCartItems(data.cartItems);
      else setCartItems([]);
    } catch (err) {
      console.error("Failed to load cart:", err.message);
      setCartItems([]);
    }
  };

  const loadProfileName = async () => {
    const token = AuthService.getToken();
    if (!token) return;

    try {
      const response = await fetch(`${API_BASE_URL}/profile`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) return;

      const data = await response.json();

      if (data?.name) {
        setDisplayName(data.name);

        const existingUser = JSON.parse(localStorage.getItem("user") || "{}");
        localStorage.setItem(
          "user",
          JSON.stringify({
            ...existingUser,
            name: data.name,
          }),
        );
      }
    } catch (err) {
      console.error("Failed to load profile name:", err);
    }
  };

  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      await loadProducts();

      if (AuthService.isAuthenticated()) {
        await Promise.all([loadCart(), loadProfileName()]);
      }

      setLoading(false);
    };

    loadData();
  }, []);

  const handleCartUpdated = async (message) => {
    setCartMessage(message);
    await loadCart();

    setTimeout(() => {
      setCartMessage("");
    }, 2000);
  };

  const filteredProducts = useMemo(() => {
    return products.filter((product) => {
      const name = (product.name || "").toLowerCase();
      const description = (product.description || "").toLowerCase();
      const category = (
        product.categoryName ||
        product.category ||
        ""
      ).toLowerCase();

      const matchesSearch =
        name.includes(searchTerm.toLowerCase()) ||
        description.includes(searchTerm.toLowerCase());

      const matchesCategory =
        selectedCategory === "All" ||
        category === selectedCategory.toLowerCase();

      return matchesSearch && matchesCategory;
    });
  }, [products, searchTerm, selectedCategory]);

  const categories = useMemo(() => {
    const categoryList = products
      .map((product) => product.categoryName || product.category)
      .filter(Boolean);

    return ["All", ...new Set(categoryList)];
  }, [products]);

  const cartCount = cartItems.reduce(
    (sum, item) => sum + (item.quantity || 1),
    0,
  );

  return (
    <div className="homepage-shell">
      <HomeHeader
        onLogout={handleLogout}
        cartCount={cartCount}
        searchTerm={searchTerm}
        setSearchTerm={setSearchTerm}
        selectedCategory={selectedCategory}
        setSelectedCategory={setSelectedCategory}
        categories={categories}
        onCartClick={() => navigate("/cart")}
        onProfileClick={() => navigate("/profile")}
        onOrdersClick={() => navigate("/myorders")}
        onAdminDashboardClick={() => navigate("/admin/products")}
        userName={displayName}
        isAdmin={isAdmin}
      />

      <main className="homepage-scroll-area">
        <div className="homepage-content">
          <HeroSection />

          {cartMessage && <div className="cart-toast">{cartMessage}</div>}

          <ProductList
            products={filteredProducts}
            loading={loading}
            error={error}
            onCartUpdated={handleCartUpdated}
          />
        </div>
      </main>

      <Footer />
    </div>
  );
}
