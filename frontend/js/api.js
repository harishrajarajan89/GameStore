const API_ORIGIN = window.location.hostname === 'localhost'
  ? 'http://localhost:8080'
  : '';

const BASE_URL = `${API_ORIGIN}/api`;

const Auth = {
  getUser() {
    try {
      return JSON.parse(sessionStorage.getItem('user'));
    } catch {
      return null;
    }
  },
  setUser(user) {
    sessionStorage.setItem('user', JSON.stringify(user));
    window.dispatchEvent(new Event('auth-changed'));
  },
  clearUser() {
    sessionStorage.removeItem('user');
    sessionStorage.removeItem('credentials');
    window.dispatchEvent(new Event('auth-changed'));
  },

  getCredentials() {
    return sessionStorage.getItem('credentials');
  },

  setCredentials(username, password) {
    const encoded = btoa(`${username}:${password}`);
    sessionStorage.setItem('credentials', encoded);
  },
  isLoggedIn() {
    return !!this.getUser();
  },
  isAdmin() {
    const user = this.getUser();
    return user && user.role === 'ADMIN';
  }
};

async function apiFetch(path, options = {}) {
  const headers = { 'Content-Type': 'application/json', ...options.headers };

  const creds = Auth.getCredentials();
  if (creds) {
    headers['Authorization'] = `Basic ${creds}`;
  }

  const res = await fetch(`${BASE_URL}${path}`, {
    ...options,
    headers
  });

  if (res.status === 204) return null;

  let data;
  try {
    data = await res.json();
  } catch {
    data = null;
  }

  if (!res.ok) {
    const msg = data?.message || data?.error || `HTTP ${res.status}`;
    throw new Error(msg);
  }

  return data;
}

const AuthAPI = {
  register: (payload) => apiFetch('/auth/register', { method: 'POST', body: JSON.stringify(payload) }),
  login:    (payload) => apiFetch('/auth/login',    { method: 'POST', body: JSON.stringify(payload) }),
  logout:   ()        => apiFetch('/auth/logout',   { method: 'POST' }),
  me:       ()        => apiFetch('/auth/me')
};

const GamesAPI = {
  getAll(params = {}) {
    const q = new URLSearchParams(params).toString();
    return apiFetch(`/games${q ? '?' + q : ''}`);
  },
  getById:  (id)          => apiFetch(`/games/${id}`),
  create:   (payload)     => apiFetch('/games',       { method: 'POST',   body: JSON.stringify(payload) }),
  update:   (id, payload) => apiFetch(`/games/${id}`, { method: 'PUT',    body: JSON.stringify(payload) }),
  delete:   (id)          => apiFetch(`/games/${id}`, { method: 'DELETE' })
};

const CartAPI = {
  get:        ()            => apiFetch('/cart'),
  addItem:    (payload)     => apiFetch('/cart/items',           { method: 'POST',   body: JSON.stringify(payload) }),
  updateItem: (itemId, qty) => apiFetch(`/cart/items/${itemId}?quantity=${qty}`, { method: 'PUT' }),
  removeItem: (itemId)      => apiFetch(`/cart/items/${itemId}`, { method: 'DELETE' }),
  clear:      ()            => apiFetch('/cart',                 { method: 'DELETE' })
};

const OrdersAPI = {
  checkout: ()    => apiFetch('/orders/checkout', { method: 'POST' }),
  getAll:   ()    => apiFetch('/orders'),
  getById:  (id)  => apiFetch(`/orders/${id}`)
};

const AdminAPI = {
  getAllOrders:       ()           => apiFetch('/admin/orders'),
  updateOrderStatus: (id, status) => apiFetch(`/admin/orders/${id}/status?status=${status}`, { method: 'PUT' })
};
