import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaEdit, FaTrash } from "react-icons/fa";
import "./AddProduct.css";

const API_BASE_URL = "http://localhost:8081/api/v1.0";

const initialForm = {
  name: "",
  description: "",
  price: "",
  brand: "",
  category: "",
  stock: "",
  imageUrl: "",
  isActive: true,
};

export default function AddProduct() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState(initialForm);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pageLoading, setPageLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [editingProductId, setEditingProductId] = useState(null);

  const fetchProducts = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/products`);
      if (!response.ok) {
        throw new Error("Failed to fetch products");
      }
      const data = await response.json();
      setProducts(data);
    } catch {
      setError("Unable to load products");
    } finally {
      setPageLoading(false);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const validateForm = () => {
    if (!formData.name.trim()) return "Product name is required";
    if (!formData.description.trim()) return "Description is required";
    if (!formData.price || Number(formData.price) <= 0)
      return "Valid price is required";
    if (!formData.category.trim()) return "Category is required";
    if (!formData.stock || Number(formData.stock) < 0)
      return "Valid stock is required";
    return "";
  };

  const resetForm = () => {
    setFormData(initialForm);
    setEditingProductId(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setMessage("");
    setError("");

    const validationError = validateForm();
    if (validationError) {
      setError(validationError);
      return;
    }

    setLoading(true);

    try {
      const url = editingProductId
        ? `${API_BASE_URL}/products/${editingProductId}`
        : `${API_BASE_URL}/products`;

      const method = editingProductId ? "PUT" : "POST";

      const response = await fetch(url, {
        method,
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          ...formData,
          price: Number(formData.price),
          stock: Number(formData.stock),
        }),
      });

      if (!response.ok) {
        throw new Error(
          editingProductId
            ? "Failed to update product"
            : "Failed to create product",
        );
      }

      await response.json();

      setMessage(
        editingProductId
          ? "Product updated successfully"
          : "Product added successfully",
      );
      resetForm();
      fetchProducts();
    } catch {
      setError(
        editingProductId
          ? "Failed to update product"
          : "Failed to save product",
      );
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (product) => {
    setEditingProductId(product.productId);
    setFormData({
      name: product.name || "",
      description: product.description || "",
      price: product.price || "",
      brand: product.brand || "",
      category: product.category || "",
      stock: product.stock || "",
      imageUrl: product.imageUrl || "",
      isActive: product.isActive ?? true,
    });

    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleDelete = async (productId) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this product?",
    );
    if (!confirmDelete) return;

    try {
      const response = await fetch(`${API_BASE_URL}/products/${productId}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        throw new Error("Failed to delete product");
      }

      setMessage("Product deleted successfully");
      fetchProducts();
    } catch {
      setError("Failed to delete product");
    }
  };

  return (
    <div className="admin-page">
      <header className="admin-header">
        <div>
          <h1>Admin Product Panel</h1>
          <p>Manage products for your e-commerce store</p>
        </div>

        <button className="back-button" onClick={() => navigate("/home")}>
          Back to Home
        </button>
      </header>

      <div className="admin-content">
        <section className="product-form-section">
          <h2>{editingProductId ? "Edit Product" : "Add Product"}</h2>

          {message && <div className="alert success">{message}</div>}
          {error && <div className="alert error">{error}</div>}

          <form className="product-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Product Name</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                placeholder="Enter product name"
              />
            </div>

            <div className="form-group">
              <label>Description</label>
              <textarea
                name="description"
                value={formData.description}
                onChange={handleChange}
                placeholder="Enter product description"
                rows="4"
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Price</label>
                <input
                  type="number"
                  name="price"
                  value={formData.price}
                  onChange={handleChange}
                  placeholder="Enter price"
                />
              </div>

              <div className="form-group">
                <label>Stock</label>
                <input
                  type="number"
                  name="stock"
                  value={formData.stock}
                  onChange={handleChange}
                  placeholder="Enter stock quantity"
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Brand</label>
                <input
                  type="text"
                  name="brand"
                  value={formData.brand}
                  onChange={handleChange}
                  placeholder="Enter brand name"
                />
              </div>

              <div className="form-group">
                <label>Category</label>
                <input
                  type="text"
                  name="category"
                  value={formData.category}
                  onChange={handleChange}
                  placeholder="Enter category"
                />
              </div>
            </div>

            <div className="form-group">
              <label>Image URL</label>
              <input
                type="text"
                name="imageUrl"
                value={formData.imageUrl}
                onChange={handleChange}
                placeholder="Paste image URL"
              />
            </div>

            <div className="checkbox-group">
              <input
                type="checkbox"
                id="isActive"
                name="isActive"
                checked={formData.isActive}
                onChange={handleChange}
              />
              <label htmlFor="isActive">Active product</label>
            </div>

            <div className="form-actions">
              <button
                type="submit"
                className="submit-button"
                disabled={loading}
              >
                {loading
                  ? "Saving..."
                  : editingProductId
                    ? "Update Product"
                    : "Add Product"}
              </button>

              {editingProductId && (
                <button
                  type="button"
                  className="cancel-button"
                  onClick={resetForm}
                >
                  Cancel Edit
                </button>
              )}
            </div>
          </form>
        </section>

        <section className="product-list-section">
          <h2>Product List</h2>

          {pageLoading ? (
            <p className="info-text">Loading products...</p>
          ) : products.length === 0 ? (
            <p className="info-text">No products found.</p>
          ) : (
            <div className="table-wrapper">
              <table className="product-table">
                <thead>
                  <tr>
                    <th>Image</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Brand</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Status</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {products.map((product) => (
                    <tr key={product.productId}>
                      <td>
                        <img
                          src={
                            product.imageUrl ||
                            "https://placehold.co/60x60?text=No+Img"
                          }
                          alt={product.name}
                          className="table-product-image"
                        />
                      </td>
                      <td>{product.name}</td>
                      <td>{product.category}</td>
                      <td>{product.brand || "—"}</td>
                      <td>₹ {product.price}</td>
                      <td>{product.stock}</td>
                      <td>
                        <span
                          className={
                            product.isActive
                              ? "status active"
                              : "status inactive"
                          }
                        >
                          {product.isActive ? "Active" : "Inactive"}
                        </span>
                      </td>
                      <td>
                        <div className="action-buttons">
                          <button
                            type="button"
                            className="icon-btn edit-btn"
                            onClick={() => handleEdit(product)}
                            title="Edit product"
                            aria-label="Edit product"
                          >
                            <FaEdit />
                          </button>

                          <button
                            type="button"
                            className="icon-btn delete-btn"
                            onClick={() => handleDelete(product.productId)}
                            title="Delete product"
                            aria-label="Delete product"
                          >
                            <FaTrash />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </div>
    </div>
  );
}
