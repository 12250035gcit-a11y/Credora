# Credora Backend

Spring Boot + PostgreSQL API for the Credora CV builder / skill-gap frontend.
Full write-up of the code and the database design: `Credora-Backend-Documentation.docx`
in the project root.

## Run everything with Docker (recommended)

```bash
cd backend
docker compose up --build
```

This starts PostgreSQL and the API together. The API is then available at
`http://localhost:8080`, and the frontend (started separately with `npm run dev`
from the project root) can talk to it - CORS already allows `http://localhost:5173`.

## Run locally without Docker

Requires Java 17+, Maven, and a local PostgreSQL with a `credora` database/user
(or just run `docker compose up db` to get only the database in a container).

```bash
cd backend
cp .env.example .env   # adjust if needed
mvn spring-boot:run
```

## Key endpoints

| Method | Path                      | Auth | Purpose                         |
|--------|---------------------------|------|----------------------------------|
| POST   | /api/auth/signup          | no   | Create an account                |
| POST   | /api/auth/login           | no   | Log in, get a token              |
| GET    | /api/templates            | no   | The 3 CV templates               |
| GET    | /api/cv                   | yes  | Get the logged-in user's CV      |
| PUT    | /api/cv                   | yes  | Create/update the CV             |
| POST   | /api/cv/undo              | yes  | Undo the last CV change          |
| GET    | /api/jobs?q=              | no   | List/search jobs                 |
| GET    | /api/jobs/categories      | no   | Career category tree             |
| GET    | /api/jobs/{id}/skill-gap  | yes  | Missing skills for one job       |
| GET    | /api/jobs/match           | yes  | All jobs ranked by skill match   |

Send the token from login/signup as `Authorization: Bearer <token>` on every
`yes`-auth request above.
