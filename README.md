# SOE Asset Management System

A standardized platform for managing fixed assets, consumable stock, handovers, and liquidations for State-Owned Enterprises (SOEs), ensuring compliance with national accounting and equipment oversight regulations.

---

## Team & Ownership

| Member | Role | Responsible For |
|--------|------|-----------------|
| **M1** | Project Manager · Backend Foundation | Auth, RBAC, DB schema, config, shared infra |
| **M2** | Fixed Assets Module | FA-01 to FA-04 — asset CRUD, depreciation |
| **M3** | Consumable Stock Module | CS-01 to CS-04 — materials, stock transactions |
| **M4** | Handover · Liquidation · Audit · Reporting | HL-01 to HL-03, RP-01 to RP-03 |
| **M5** | Frontend (All Modules) | React + TypeScript + Ant Design UI |

---

## Project Structure

```
soe-asset-management/
├── backend/        -> Spring Boot REST API      [M1 foundation, M2/M3/M4 modules]
├── frontend/       -> React + TypeScript (Vite) [M5]
└── docs/           -> API spec, DB schema, diagrams [shared]
```

---

## Prerequisites
- Java 17+, Maven 3.8+
- Node.js 20+, npm 9+
- Docker Desktop
- PostgreSQL run via Docker


## System Launcher
- Database    -> `http://localhost:5432`
- Backend API -> `http://localhost:8080`
- Frontend    -> `http://localhost:5173`

### Database
```bash
docker-compose up --build # Build Docker
docker-compose up -d # Launch database via Docker
```

The database will be available at `http://localhost:5432`.

### Backend
```bash
cd backend 
./mvnw clean install # Install dependencies
./mvnw spring-boot:run # Start the server
```

The backend will be available at `http://localhost:8080`.

### Frontend
```bash
cd frontend 
npm install # Install dependencies
npm run dev # Start the development server
```

The frontend will be available at `http://localhost:5173`.

---

# Documents

| Document | Location |
|----------|----------|
| System Architecture | [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) |
| API Specification | [docs/api-spec.md](docs/api-spec.md) |
| Database Schema | [docs/database-schema.md](docs/database-schema.md) |
| ERD Diagram | [docs/diagrams/erd.png](docs/diagrams/erd.png) |
| Architecture Diagram | [docs/diagrams/soe-aims-architecture](docs/diagrams/soe-aims-architecture.png) |
| Use Case Diagram | [docs/diagrams/soe-aims-general-use-case](docs/diagrams/soe-aims-general-use-case.png) |