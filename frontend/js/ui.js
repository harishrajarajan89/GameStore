function showToast(message, type = 'info', duration = 3000) {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    container.style.cssText = `
      position: fixed; bottom: 24px; right: 24px;
      display: flex; flex-direction: column; gap: 10px;
      z-index: 9999; pointer-events: none;
    `;
    document.body.appendChild(container);
  }

  const colors = { success: '#00e676', error: '#ff4444', info: '#00e5ff', warning: '#ffab40' };
  const icons  = { success: '✓', error: '✕', info: 'ℹ', warning: '⚠' };

  const toast = document.createElement('div');
  toast.style.cssText = `
    background: #1a1a28; border: 1px solid ${colors[type] || colors.info};
    border-left: 3px solid ${colors[type] || colors.info};
    color: #f0f0ff; padding: 12px 18px; border-radius: 8px;
    font-family: Rajdhani, sans-serif; font-size: 0.95rem; font-weight: 500;
    display: flex; align-items: center; gap: 10px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.5);
    pointer-events: auto; cursor: pointer;
    animation: slideIn 0.3s ease;
    max-width: 340px;
  `;

  if (!document.getElementById('toast-style')) {
    const s = document.createElement('style');
    s.id = 'toast-style';
    s.textContent = `
      @keyframes slideIn  { from { transform: translateX(120%); opacity: 0; } to { transform: translateX(0); opacity: 1; } }
      @keyframes slideOut { from { transform: translateX(0); opacity: 1; } to { transform: translateX(120%); opacity: 0; } }
    `;
    document.head.appendChild(s);
  }

  toast.innerHTML = `
    <span style="color:${colors[type]};font-weight:700;">${icons[type]}</span>
    <span>${message}</span>
  `;

  container.appendChild(toast);
  toast.onclick = () => removeToast(toast);
  setTimeout(() => removeToast(toast), duration);
}

function removeToast(el) {
  el.style.animation = 'slideOut 0.3s ease forwards';
  setTimeout(() => el.remove(), 300);
}

function showAlert(containerId, message, type = 'error') {
  const el = document.getElementById(containerId);
  if (!el) return;
  el.innerHTML = `<div class="alert alert-${type}">${message}</div>`;
}

function clearAlert(containerId) {
  const el = document.getElementById(containerId);
  if (el) el.innerHTML = '';
}

function setLoading(btn, loading, text = 'Loading...') {
  if (!btn) return;
  if (loading) {
    btn.dataset.originalText = btn.textContent;
    btn.textContent = text;
    btn.disabled = true;
  } else {
    btn.textContent = btn.dataset.originalText || text;
    btn.disabled = false;
  }
}

function renderNavbar(activePage = '') {
  const user = Auth.getUser();
  const isLoggedIn = !!user;
  const isAdmin = user?.role === 'ADMIN';

  const navbar = document.getElementById('navbar');
  if (!navbar) return;

  navbar.innerHTML = `
    <nav class="navbar">
      <div class="container">
        <a href="/index.html" class="nav-logo">GAME<span>VAULT</span></a>
        <ul class="nav-links">
          <li><a href="/index.html" class="${activePage === 'home' ? 'active' : ''}">
            🎮 <span class="nav-text">Store</span>
          </a></li>

          ${isLoggedIn ? `
            <li class="cart-badge">
              <a href="/pages/cart.html" class="${activePage === 'cart' ? 'active' : ''}">
                🛒 <span class="nav-text">Cart</span>
                <span class="cart-count" id="cart-count">0</span>
              </a>
            </li>
            <li><a href="/pages/orders.html" class="${activePage === 'orders' ? 'active' : ''}">
              📦 <span class="nav-text">Orders</span>
            </a></li>
          ` : ''}

          ${isAdmin ? `
            <li><a href="/pages/admin.html" class="${activePage === 'admin' ? 'active' : ''}">
              ⚙️ <span class="nav-text">Admin</span>
            </a></li>
          ` : ''}

          ${isLoggedIn ? `
            <li>
              <button class="btn btn-secondary btn-sm" onclick="handleLogout()">
                Logout (${user.username})
              </button>
            </li>
          ` : `
            <li><a href="/pages/login.html" class="${activePage === 'login' ? 'active' : ''}">Login</a></li>
            <li><a href="/pages/register.html" class="${activePage === 'register' ? 'active' : ''}">
              <button class="btn btn-primary btn-sm">Register</button>
            </a></li>
          `}
        </ul>
      </div>
    </nav>
  `;

  if (isLoggedIn) updateCartCount();
}

async function updateCartCount() {
  if (!Auth.isLoggedIn()) return;
  try {
    const cart = await CartAPI.get();
    const count = cart?.items?.length || 0;
    const badge = document.getElementById('cart-count');
    if (badge) {
      badge.textContent = count;
      badge.style.display = count > 0 ? 'flex' : 'none';
    }
  } catch {}
}

async function handleLogout() {
  try { await AuthAPI.logout(); } catch {}
  Auth.clearUser();
  showToast('Logged out successfully', 'info');
  setTimeout(() => window.location.href = '/index.html', 500);
}

function formatPrice(price) {
  return `Rs ${parseFloat(price).toLocaleString('en-PK', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })}`;
}

function formatDate(dateStr) {
  if (!dateStr) return '—';
  return new Date(dateStr).toLocaleDateString('en-US', {
    year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit'
  });
}

function requireAuth(redirectTo = '/pages/login.html') {
  if (!Auth.isLoggedIn()) {
    window.location.href = redirectTo;
    return false;
  }
  return true;
}

function requireAdmin() {
  if (!Auth.isAdmin()) {
    showToast('Admin access required', 'error');
    setTimeout(() => window.location.href = '/index.html', 800);
    return false;
  }
  return true;
}

function openModal(id) {
  const m = document.getElementById(id);
  if (m) {
    m.classList.add('active');
    document.body.style.overflow = 'hidden';
  }
}

function closeModal(id) {
  const m = document.getElementById(id);
  if (m) {
    m.classList.remove('active');
    document.body.style.overflow = '';
  }
}

document.addEventListener('click', (e) => {
  if (e.target.classList.contains('modal-overlay')) {
    e.target.classList.remove('active');
    document.body.style.overflow = '';
  }
});
