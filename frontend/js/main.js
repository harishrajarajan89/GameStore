function createGameCard(game) {
  return `
    <div class="game-card">
      <div class="game-image">
        ${game.imageUrl ? `<img src="${game.imageUrl}" alt="${game.title}">` : '🎮'}
      </div>
      <div class="game-info">
        <div class="game-title">${game.title}</div>
        <div class="game-genre">${game.genre || 'N/A'}</div>
        <div class="game-platform">${game.platform || 'N/A'}</div>
        <div class="game-description">${game.description}</div>
        <div class="game-price">${formatPrice(game.price)}</div>
        <div class="game-stock ${game.stock < 5 ? 'low' : ''}">
          Stock: ${game.stock > 0 ? game.stock : 'Out of Stock'}
        </div>
        <div class="game-actions">
          <button class="btn btn-primary" onclick="addGameToCart(${game.id})"
            ${game.stock <= 0 ? 'disabled' : ''}>
            Add to Cart
          </button>
        </div>
      </div>
    </div>
  `;
}

async function addGameToCart(gameId) {
  if (!Auth.isLoggedIn()) {
    showToast('Please login first', 'info');
    window.location.href = '/pages/login.html';
    return;
  }
  try {
    await CartAPI.addItem({ gameId, quantity: 1 });
    showToast('Added to cart!', 'success');
    updateCartCount();
  } catch (err) {
    showToast(err.message, 'error');
  }
}

async function loadCart() {
  const cartItemsTable = document.getElementById('cartItems');
  if (!cartItemsTable) return;

  if (!Auth.isLoggedIn()) {
    cartItemsTable.innerHTML = '<tr><td colspan="5">Please login to view cart</td></tr>';
    return;
  }

  try {
    const cart = await CartAPI.get();
    if (!cart || cart.items.length === 0) {
      cartItemsTable.innerHTML = '<tr><td colspan="5">Your cart is empty</td></tr>';
      return;
    }

    let total = 0;
    let html = '';

    cart.items.forEach(item => {
      const itemTotal = item.unitPrice * item.quantity;
      total += itemTotal;
      html += `
        <tr>
          <td>${item.gameTitle}</td>
          <td>${formatPrice(item.unitPrice)}</td>
          <td>
            <input type="number" value="${item.quantity}" min="1"
              onchange="updateQuantity(${item.id}, this.value)">
          </td>
          <td>${formatPrice(itemTotal)}</td>
          <td>
            <button class="btn btn-danger" onclick="removeItemFromCart(${item.id})">Remove</button>
          </td>
        </tr>
      `;
    });

    cartItemsTable.innerHTML = html;
    document.getElementById('total').textContent = formatPrice(total);
  } catch (err) {
    showToast(err.message, 'error');
  }
}

async function updateQuantity(cartItemId, newQuantity) {
  try {
    await CartAPI.updateItem(cartItemId, newQuantity);
    showToast('Cart updated', 'success');
    loadCart();
  } catch (err) {
    showToast(err.message, 'error');
  }
}
async function removeItemFromCart(cartItemId) {
  try {
    await CartAPI.removeItem(cartItemId);
    showToast('Item removed', 'success');
    loadCart();
  } catch (err) {
    showToast(err.message, 'error');
  }
}
