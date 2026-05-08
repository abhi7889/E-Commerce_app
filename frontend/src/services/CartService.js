import AuthService from "./AuthService";

const API_BASE_URL = "http://localhost:8081/api/v1.0";

const getAuthHeaders = () => {
  const token = AuthService.getToken?.();
  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };
};

const CartService = {
  async addToCart(productId, quantity = 1) {
    const response = await fetch(`${API_BASE_URL}/cart/items`, {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify({ productId, quantity }),
    });

    if (!response.ok) {
      throw new Error("Failed to add to cart");
    }

    return response.json();
  },

  async getCart() {
    const response = await fetch(`${API_BASE_URL}/cart`, {
      method: "GET",
      headers: getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error("Failed to fetch cart");
    }

    return response.json();
  },

  async removeCartItem(cartItemId) {
    const response = await fetch(`${API_BASE_URL}/cart/items/${cartItemId}`, {
      method: "DELETE",
      headers: getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error("Failed to remove cart item");
    }

    return true;
  },
};

export default CartService;