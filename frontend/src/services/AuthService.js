const AuthService = {
  setToken: (token) => {
    localStorage.setItem('jwtToken', token);
  },

  getToken: () => {
    return localStorage.getItem('jwtToken');
  },

  removeToken: () => {
    localStorage.removeItem('jwtToken');
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('jwtToken');
  },

  logout: () => {
    localStorage.removeItem('jwtToken');
  },
};

export default AuthService;