import AuthService from "./AuthService";

const API_BASE_URL = "http://localhost:8081/api/v1.0";

const getAuthHeaders = () => {
  const token = AuthService.getToken();
  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };
};

const CartService = {
  async getCart() {
    const response = await fetch(`${API_BASE_URL}/cart`, {
      method: "GET",
      headers: getAuthHeaders(),
      credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Failed to fetch cart");
    }

    return response.json();
  },

  async addToCart(productId, quantity) {
    const response = await fetch(`${API_BASE_URL}/cart`, {
      method: "POST",
      headers: getAuthHeaders(),
      credentials: "include",
      body: JSON.stringify({ productId, quantity }),
    });

    if (!response.ok) {
      throw new Error("Failed to add to cart");
    }

    return response.json();
  },

  async removeCartItem(cartItemId) {
    const response = await fetch(`${API_BASE_URL}/cart/${cartItemId}`, {
      method: "DELETE",
      headers: getAuthHeaders(),
      credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Failed to remove item");
    }
  },

  async updateCartItem(cartItemId, quantity) {
    const response = await fetch(`${API_BASE_URL}/cart/${cartItemId}`, {
      method: "PUT",
      headers: getAuthHeaders(),
      credentials: "include",
      body: JSON.stringify({ quantity }),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Failed to update quantity");
    }

    return response.json();
  },
};

export default CartService;