const AuthService = {
  setToken(token) {
    localStorage.setItem("token", token);
  },

  getToken() {
    return localStorage.getItem("token");
  },

  setUser(user) {
    localStorage.setItem("user", JSON.stringify(user));
  },

  getUser() {
    try {
      return JSON.parse(localStorage.getItem("user") || "{}");
    } catch {
      return {};
    }
  },

  isAuthenticated() {
    return !!localStorage.getItem("token");
  },

  logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
  },
};

export default AuthService;