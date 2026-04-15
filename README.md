# GameVault

This is a full-stack game store project built using Spring Boot for the backend and a simple HTML/CSS/JavaScript frontend.

## Overview

This project is a basic e-commerce style application where users can browse games, add them to cart, and place orders. It also has an admin side to manage games.

### Tech Stack

* Backend: Spring Boot (Java)
* Database: PostgreSQL (Neon DB)
* Frontend: HTML, CSS, JavaScript (no framework)
* Image Storage: Cloudinary
* Authentication: Basic Auth (Spring Security)

------------
## Credentials
Admin: admin / admin123
User: testuser / password123
----------------

## DEMO Link
https://drive.google.com/file/d/1KdFoXBHuX0-2JFpf5owHmU-kh7NmWqwm/view?usp=sharing

## Status

* Backend: Live on Render - https://gamevault-djgd.onrender.com/ 
* Frontend: Live on Vercel - https://game-store-n7qhtvvna-harishrajarajan89s-projects.vercel.app/
(Backend takes time to respond on first request due to free hosting)
* Database: Connected (Neon DB) 
Project is fully working end-to-end.

## Project Structure

```
game-store/
│-- backend/
│   │-- pom.xml
│   └── src/main/
│       ├── java/com/gamestore/
│       └── resources/
│
│-- frontend/
│   │-- index.html
│   │-- css/
│   │-- js/
│   └── pages/
│
└── README.md
```


## Features

* View all available games
* Add to cart and update quantity
* Place orders
* Admin can add/update/delete games
* Image upload using Cloudinary
* Basic login system


## Backend Info

* Main class: `GameStoreApplication.java`
* Uses Spring Data JPA for database operations
* Security handled using Spring Security
* Some default users are created when app starts:


## Frontend Info

* Main page: `index.html`
* API calls handled in: `js/api.js`
* Admin panel: `pages/admin.html`


## Deployment

* Backend deployed on Render
* Frontend deployed on Vercel

Note: Since Render free tier is used, the backend may take around 30–60 seconds to respond if it was inactive.

To fix this, I used cron-job.org to keep the backend alive.

Created a cron job that hits the backend API every 5 minutes. This prevents the server from going idle 
Result
      -Backend stays active
      -No delay on first request
      -App feels much faster
      -Related server error resolved

## Configuration

Update `application.properties` with your own values:

-----------
spring.datasource.url=YOUR_DB_URL
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

cloudinary.cloud-name=YOUR_NAME
cloudinary.api-key=YOUR_KEY
cloudinary.api-secret=YOUR_SECRET
------------


## Run Locally

### Backend

cd backend
mvn spring-boot:run

Runs on:

http://localhost:8080

### Frontend

cd frontend
node server.js

Runs on:

http://localhost:3000


## Challenges Faced

During this project, I faced several issues, especially while deployment:

* **Render build errors**

  * Initially selected Node environment instead of Java/Docker
  * Faced `mvn: command not found` error

* **Docker issues**

  * Dockerfile name was incorrect (`DockerFile` instead of `Dockerfile`)
  * Wrong Dockerfile path caused build failures

* **Path confusion**

  * Root directory and build context were misconfigured
  * Took time to understand how Render handles paths

* **Frontend deployment issues**

  * Vercel was deploying from wrong branch (`main` vs `master`)
  * Initially selected wrong root directory (not `frontend`)

* **CORS issues**

  * Frontend couldn't call backend until CORS config was fixed

* **Cold start delay**

  * Backend takes time to respond on first request due to free hosting


---

## Notes

* Backend returns image URLs for frontend display
* Admin APIs expect `image` field while creating/updating games

---

## AI Usage

Some AI tools were used during development mainly for:

* Debugging errors
* Fixing frontend issues
* Understanding deployment problems

But most of the integration and debugging was done manually.

