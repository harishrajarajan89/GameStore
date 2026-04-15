# GameVault

GameVault is a full-stack game store built with Spring Boot, PostgreSQL, and a vanilla HTML/CSS/JavaScript frontend. Users can create their own accounts, browse the catalog, manage a cart, and place orders. An optional admin account can be seeded for catalog and order management.

## Demo

Video walkthrough: [GameVault demo](https://drive.google.com/file/d/1KdFoXBHuX0-2JFpf5owHmU-kh7NmWqwm/view?usp=sharing)

## Features

* Browse the game catalog with images, pricing, genre, and platform details
* Register a new user account and log in with Spring Security Basic Auth
* Add games to cart, update quantities, and clear the cart
* Place orders and review previous orders
* Manage games and order statuses from the admin dashboard
* Upload and store cover images with Cloudinary

## Tech Stack

* Backend: Spring Boot 3, Spring Security, Spring Data JPA, Maven, Java 21
* Frontend: HTML, CSS, JavaScript
* Database: PostgreSQL
* Media storage: Cloudinary
* Hosting target: Docker-friendly backend deployable to Fly.io, Railway, or a VPS

## Account Access

Regular users do not need shared demo credentials. Open the register page and create a fresh account to explore the shopper flow.

Admin access is optional. If you want admin features in a local or hosted deployment, provide these environment variables for the backend before startup:

* `APP_SEED_ADMIN_USERNAME`
* `APP_SEED_ADMIN_EMAIL`
* `APP_SEED_ADMIN_PASSWORD`

If those values are not set, the application starts normally without creating a default admin user.

## Project Structure

```text
game-store/
|-- backend/
|   |-- pom.xml
|   |-- Dockerfile
|   `-- src/main/
|-- frontend/
|   |-- index.html
|   |-- css/
|   |-- js/
|   `-- pages/
`-- README.md
```

## Configuration

The backend reads its runtime configuration from environment variables. The required variables are:

* `DB_URL`
* `DB_USER`
* `DB_PASS`
* `CLOUDINARY_CLOUD_NAME`
* `CLOUDINARY_API_KEY`
* `CLOUDINARY_API_SECRET`

These are mapped in [application.properties](/E:/projects/game-store/backend/src/main/resources/application.properties:1).

## Run Locally

### Backend

```bash
cd backend
mvn spring-boot:run
```

The API runs at `http://localhost:8080`.

### Frontend

```bash
cd frontend
node server.js
```

The frontend runs at `http://localhost:3000`.

## Deployment Notes

The backend should be deployed to a supported always-on provider such as Fly.io, Railway paid tier, or a VPS before submission. The repository already includes a Dockerfile for container deployment.

If you deploy the frontend separately, update [frontend/vercel.json](/E:/projects/game-store/frontend/vercel.json:1) so `/api/*` rewrites point to your deployed backend domain.

## Implementation Notes

* Public catalog endpoints are open, while cart, order, and admin endpoints require authentication.
* A cart is created automatically when a new user registers.
* Image URLs are stored and returned by the backend for frontend rendering.

## AI Usage

AI assistance was used in a limited, supervised way during development for:

* debugging Spring Boot and deployment issues
* refining frontend interactions and validation