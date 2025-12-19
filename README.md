# CityBusTracker
CityBusTracker is an MVP for following intercity buses in Łódź. The backend exposes REST APIs for routes, stops, bookings, and simulated bus locations, while the frontend (Vite + React + Leaflet) visualizes everything on a live map.

## Stack
- Java 21, Spring Boot, Flyway, Hibernate
- PostgreSQL, Docker/Docker Compose
- React 18, Vite, TypeScript, Leaflet/react-leaflet
- Prometheus + Grafana (metrics/observability)
- GitHub Actions (Java 21 build/test + Docker build + frontend build)

## Quickstart (backend + db + monitoring)
```bash
docker compose up --build
```
This starts PostgreSQL, the Spring Boot backend (port `8080`), Prometheus (`9090`), and Grafana (`3000`). Prometheus automatically scrapes `http://backend:8080/actuator/prometheus` and Grafana is provisioned with a Prometheus datasource.

Health endpoints:
- Backend: http://localhost:8080
- Health: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/prometheus
- Prometheus UI: http://localhost:9090
- Grafana: http://localhost:3000 (default credentials `admin/admin`)

## Frontend dev server
```bash
cd frontend
npm install
cp .env.example .env # adjust VITE_API_BASE_URL if needed
npm run dev
```
The Vite dev server runs at http://localhost:5173 and polls the backend every 2 seconds for bus positions. The Leaflet map defaults to Łódź until a route is selected.

## Useful curl commands
Create a route:
```bash
curl -s -X POST http://localhost:8080/api/routes \
  -H "Content-Type: application/json" \
  -d '{"name":"Lodz Center - Airport","city":"Lodz"}'
```

Add stops (replace `{routeId}` with the ID from the previous command):
```bash
curl -s -X POST http://localhost:8080/api/routes/{routeId}/stops \
  -H "Content-Type: application/json" \
  -d '{"name":"Piotrkowska","lat":51.7592,"lon":19.4550,"seq":1}'

curl -s -X POST http://localhost:8080/api/routes/{routeId}/stops \
  -H "Content-Type: application/json" \
  -d '{"name":"Lodz Airport","lat":51.7219,"lon":19.3981,"seq":2}'
```

Fetch current bus positions (simulated and updated every few seconds):
```bash
curl -s "http://localhost:8080/api/buses/positions?routeId={routeId}"
```

## Notes
- Bus positions are simulated on the backend via a scheduled job; once a route has stops it will automatically receive a bus entry that moves between stops.
- GitHub Actions workflow (`.github/workflows/ci.yml`) runs `mvn test`, builds the backend Docker image, and optionally builds the frontend.
