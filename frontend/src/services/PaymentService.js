import { apiFetch } from "./api";

const PaymentService = {
  createPaymentIntent: async (amount) => {
    return await apiFetch("/payments/create-intent", {
      method: "POST",
      body: JSON.stringify({
        amount,
        currency: "inr",
      }),
    });
  },
};

export default PaymentService;