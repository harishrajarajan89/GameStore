const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const FRONTEND_DIR = __dirname;

const server = http.createServer((req, res) => {
  // Default to index.html for root
  let filePath = req.url === '/' ? '/index.html' : req.url;
  filePath = path.join(FRONTEND_DIR, filePath);

  // Security: prevent directory traversal
  const realPath = path.resolve(filePath);
  if (!realPath.startsWith(FRONTEND_DIR)) {
    res.writeHead(403, { 'Content-Type': 'text/plain' });
    res.end('Forbidden');
    return;
  }

  // Try to serve the file
  fs.readFile(filePath, (err, content) => {
    if (err) {
      // Only fallback to index.html for SPA routes (no file extension)
      if (err.code === 'ENOENT' && !path.extname(req.url)) {
        fs.readFile(path.join(FRONTEND_DIR, 'index.html'), (err, content) => {
          if (err) {
            res.writeHead(404, { 'Content-Type': 'text/plain' });
            res.end('404 Not Found');
          } else {
            res.writeHead(200, { 'Content-Type': 'text/html' });
            res.end(content);
          }
        });
      } else {
        res.writeHead(404, { 'Content-Type': 'text/plain' });
        res.end('404 Not Found');
      }
      return;
    }

    // Determine content type
    const ext = path.extname(filePath);
    const contentTypes = {
      '.html': 'text/html',
      '.css': 'text/css',
      '.js': 'text/javascript',
      '.json': 'application/json',
      '.png': 'image/png',
      '.jpg': 'image/jpeg',
      '.gif': 'image/gif',
      '.svg': 'image/svg+xml',
      '.ico': 'image/x-icon'
    };

    const contentType = contentTypes[ext] || 'text/plain';
    res.writeHead(200, { 
      'Content-Type': contentType,
      'Cache-Control': 'no-cache'
    });
    res.end(content);
  });
});

server.listen(PORT, () => {
  console.log(`\n🎮 GameVault Frontend Server running at http://localhost:${PORT}\n`);
  console.log('Backend API: http://localhost:8080');
});