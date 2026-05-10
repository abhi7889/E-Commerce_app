import { apiFetch } from "./api";

const OrderService = {
  placeOrder: async (orderPayload) => {
    return await apiFetch("/orders", {
      method: "POST",
      body: JSON.stringify(orderPayload),
    });
  },

  getMyOrders: async () => {
    return await apiFetch("/orders");
  },

  getOrderById: async (orderId) => {
    return await apiFetch(`/orders/${orderId}`);
  },
};

export default OrderService;