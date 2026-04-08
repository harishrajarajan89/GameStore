# GameVault

Full-stack game store built with Spring Boot, PostgreSQL, and a vanilla HTML/CSS/JavaScript frontend.

## Overview

- Backend: Spring Boot 3.2, Spring Security, Spring Data JPA
- Database: PostgreSQL
- Frontend: static HTML, CSS, and JavaScript
- Images: Cloudinary upload support for admin game management
- Auth: HTTP Basic Authentication
- Currency in UI: rupees (`Rs`)

## Project Structure

```text
game-store/
|-- backend/
|   |-- pom.xml
|   `-- src/main/
|       |-- java/com/gamestore/
|       |   |-- config/
|       |   |-- controller/
|       |   |-- dto/
|       |   |-- exception/
|       |   |-- model/
|       |   |-- repository/
|       |   `-- service/
|       `-- resources/
|           `-- application.properties
|-- frontend/
|   |-- index.html
|   |-- css/
|   |-- js/
|   `-- pages/
`-- README.md
```

## Backend Notes

- Main class: `backend/src/main/java/com/gamestore/GameStoreApplication.java`
- Security config: `backend/src/main/java/com/gamestore/config/SecurityConfigration.java`
- Global API error handling: `backend/src/main/java/com/gamestore/exception/GlobalExceptionHandler.java`
- Default seeded users are created by `DataInitializer`

Default users:

- Admin: `admin` / `admin123`
- User: `testuser` / `password123`

## Frontend Notes

- Main storefront: `frontend/index.html`
- Shared API client: `frontend/js/api.js`
- Shared UI helpers: `frontend/js/ui.js`
- Admin page: `frontend/pages/admin.html`

## Configuration

Edit `backend/src/main/resources/application.properties` and set your own values:

```properties
server.port=8080

spring.datasource.url=jdbc:postgresql://YOUR_HOST/YOUR_DB?sslmode=require
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

cloudinary.cloud-name=YOUR_CLOUD_NAME
cloudinary.api-key=YOUR_API_KEY
cloudinary.api-secret=YOUR_API_SECRET
```

Important:

- Do not commit real database or Cloudinary secrets.
- If you want to rebuild the schema during local testing, temporarily use:

```properties
spring.jpa.hibernate.ddl-auto=create
```

Then switch it back to `update` after the schema is recreated.

## Run Locally

### Backend

```bash
cd backend
mvn spring-boot:run
```

Backend runs at:

```text
http://localhost:8080
```

### Frontend

A Node.js static server is included:

```bash
cd frontend
node server.js
```

Frontend runs at:

```text
http://localhost:3000
```

## API Summary

### Auth

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/logout`
- `GET /api/auth/me`

### Games

- `GET /api/games`
- `GET /api/games/{id}`
- `POST /api/games`
- `PUT /api/games/{id}`
- `DELETE /api/games/{id}`

### Images

- `POST /api/images/upload`

### Cart

- `GET /api/cart`
- `POST /api/cart/items`
- `PUT /api/cart/items/{id}?quantity=2`
- `DELETE /api/cart/items/{id}`
- `DELETE /api/cart`

Example add-to-cart request:

```json
{
  "gameId": 1,
  "quantity": 1
}
```

### Orders

- `POST /api/orders/checkout`
- `GET /api/orders`
- `GET /api/orders/{id}`

### Admin

- `GET /api/admin/orders`
- `PUT /api/admin/orders/{id}/status?status=SHIPPED`

## Development Tips

- If port `8080` is already in use, stop the existing Java process or run on another port.
- If the frontend still shows old JS or CSS after a change, do a hard refresh.
- If game images do not show in orders, restart the backend after DTO or service changes.
- If you reset the schema with `ddl-auto=create`, you may need to add sample games again through the admin API or admin page.

## AI Assistance

Amazon Q Developer (AI assistant) was used during development for:

- Security fixes — identifying and resolving vulnerabilities such as hardcoded credentials, XSS risks, and path traversal issues in the frontend
- Frontend bug fixes — fixing broken HTML structure, duplicate event listeners, and inconsistent API calls across pages
- Code cleanup — removing auto-generated comments and reformatting JS/HTML files to match a consistent human-readable style
- README — writing and formatting the project documentation 

## Known Current Behavior

- Prices are displayed in rupees in the UI.
- Orders return game image URLs for rendering on the orders page.
- Admin game creation and update expect the backend field name `image`.

srftwsrtstxfgdst5yfyuy rtyur5yeytt5twrw