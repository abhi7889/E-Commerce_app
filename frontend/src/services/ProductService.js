import { apiFetch } from "./api";

const ProductService = {
  getAllProducts: async () => {
    return await apiFetch("/products");
  },
};

export default ProductService;