import { useMemo, useState } from "react";
import type { Route } from "../api/types";

type RoutesListProps = {
  routes: Route[];
  loading: boolean;
  error?: string;
  selectedRouteId: number | null;
  onSelect: (id: number) => void;
};

export function RoutesList({ routes, loading, error, selectedRouteId, onSelect }: RoutesListProps) {
  const [query, setQuery] = useState("");

  const filtered = useMemo(() => {
    if (!query.trim()) return routes;
    const lower = query.trim().toLowerCase();
    return routes.filter(route => `${route.name} ${route.city}`.toLowerCase().includes(lower));
  }, [routes, query]);

  return (
    <aside className="sidebar">
      <h1>CityBus Tracker</h1>
      <input
        className="search-input"
        placeholder="Search routes..."
        value={query}
        onChange={e => setQuery(e.target.value)}
      />
      <div className="routes-list">
        {loading && <span>Loading routes...</span>}
        {error && <span style={{ color: "#ef4444" }}>{error}</span>}
        {!loading && !error && filtered.length === 0 && <span>No routes found.</span>}
        {!loading &&
          !error &&
          filtered.map(route => (
            <button
              key={route.id}
              type="button"
              className={`route-card ${selectedRouteId === route.id ? "active" : ""}`}
              onClick={() => onSelect(route.id)}
            >
              <h2>{route.name}</h2>
              <span>{route.city}</span>
            </button>
          ))}
      </div>
    </aside>
  );
}
