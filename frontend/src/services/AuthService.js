const AuthService = {
  setToken: (token) => {
    localStorage.setItem('jwtToken', token);
  },

  getToken: () => {
    return localStorage.getItem('jwtToken') || getCookie('jwt');
  },

  removeToken: () => {
    localStorage.removeItem('jwtToken');
    clearCookie('jwt');
  },

  isAuthenticated: () => {
    return !!AuthService.getToken();
  },

  logout: () => {
    AuthService.removeToken();
  },
};

const getCookie = (name) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
};

const clearCookie = (name) => {
  document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
};

export default AuthService;
