import { apiFetch } from "./api";

const CartService = {
  getCart: async () => {
    return await apiFetch("/cart");
  },

  addToCart: async (productId, quantity = 1) => {
    return await apiFetch("/cart/items", {
      method: "POST",
      body: JSON.stringify({ productId, quantity }),
    });
  },

  updateCartItem: async (itemId, quantity) => {
    return await apiFetch(`/cart/items/${itemId}`, {
      method: "PUT",
      body: JSON.stringify({ quantity }),
    });
  },

  removeCartItem: async (itemId) => {
    return await apiFetch(`/cart/items/${itemId}`, {
      method: "DELETE",
    });
  },
};

export default CartService;