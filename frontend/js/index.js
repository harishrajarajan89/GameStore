document.addEventListener('DOMContentLoaded', () => {
  renderNavbar('home');
  loadGames();

  document.getElementById('search-btn').addEventListener('click', onSearch);
  document.getElementById('clear-btn').addEventListener('click', onClear);
  document.getElementById('search-input').addEventListener('keydown', (e) => {
    if (e.key === 'Enter') onSearch();
  });
  document.getElementById('genre-filter').addEventListener('change', onSearch);
});

async function loadGames(params = {}) {
  const container = document.getElementById('games-container');
  container.innerHTML = '<div class="loading-state"><div class="spinner"></div><p>Loading games...</p></div>';
  try {
    const games = await GamesAPI.getAll(params);
    renderGames(games);
  } catch (err) {
    container.innerHTML = `<div class="empty-state"><h3>Failed to load games</h3><p>${err.message}</p></div>`;
  }
}

function renderGames(games) {
  const container = document.getElementById('games-container');
  if (!games || games.length === 0) {
    container.innerHTML = '<div class="empty-state"><h3>No games found</h3><p>Try a different search.</p></div>';
    return;
  }
  container.innerHTML = `<div class="games-grid">${games.map(gameCard).join('')}</div>`;
}

function gameCard(g) {
  const stockClass = g.stock === 0 ? 'out' : g.stock < 5 ? 'low' : '';
  const stockText  = g.stock === 0 ? 'Out of stock' : g.stock < 5 ? `Only ${g.stock} left!` : `${g.stock} in stock`;
  const img = g.imageUrl || 'https://via.placeholder.com/200x266?text=No+Image';
  return `
    <div class="card game-card">
      <img class="game-card-img" src="${img}" alt="${g.title}" onerror="this.src='https://via.placeholder.com/200x266?text=No+Image'"/>
      <div class="game-card-body">
        <div class="game-card-title">${g.title}</div>
        <div class="game-card-meta">
          ${g.genre    ? `<span class="badge badge-genre">${g.genre}</span>` : ''}
          ${g.platform ? `<span class="badge badge-platform">${g.platform}</span>` : ''}
        </div>
        <div class="game-card-price">${formatPrice(g.price)}</div>
        <div class="game-card-stock ${stockClass}">${stockText}</div>
        <div style="display:flex;gap:8px;margin-top:auto;padding-top:10px;">
          <a href="/pages/game-details.html?id=${g.id}" class="btn btn-secondary btn-sm" style="flex:1;text-align:center;">Details</a>
          ${g.stock > 0
            ? `<button class="btn btn-primary btn-sm" onclick="addToCart(${g.id}, event)" style="flex:1;">Add</button>`
            : `<button class="btn btn-sm" disabled style="flex:1;opacity:0.3;">Sold Out</button>`
          }
        </div>
      </div>
    </div>
  `;
}

async function addToCart(gameId, event) {
  if (!Auth.isLoggedIn()) {
    showToast('Please login to add to cart', 'warning');
    setTimeout(() => window.location.href = '/pages/login.html', 800);
    return;
  }
  const btn = event?.target;
  setLoading(btn, true, '...');
  try {
    await CartAPI.addItem({ gameId, quantity: 1 });
    showToast('Added to cart!', 'success');
    updateCartCount();
  } catch (err) {
    showToast(err.message, 'error');
  } finally {
    setLoading(btn, false, 'Add');
  }
}

function onSearch() {
  const search = document.getElementById('search-input').value.trim();
  const genre  = document.getElementById('genre-filter').value;
  const params = {};
  if (search) params.search = search;
  else if (genre) params.genre = genre;
  loadGames(params);
}

function onClear() {
  document.getElementById('search-input').value = '';
  document.getElementById('genre-filter').value = '';
  loadGames();
}
