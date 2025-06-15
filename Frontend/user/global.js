function isLoggedIn() {
  const loginState = localStorage.getItem('loginState');
  if (!loginState) {
    window.location.href = 'register.html';
  }
}

function logout() {
  localStorage.removeItem('loginState');
  window.location.href = 'register.html';
}

window.auth = {
  isLoggedIn,
  logout,
};
