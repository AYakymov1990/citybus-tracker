import { useEffect, useState } from "react";
import { RoutesList } from "./components/RoutesList";
import { MapView } from "./components/MapView";
import type { Route } from "./api/types";
import { getRoutes } from "./api/client";

function App() {
  const [routes, setRoutes] = useState<Route[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedRouteId, setSelectedRouteId] = useState<number | null>(null);

  useEffect(() => {
    let cancelled = false;
    getRoutes()
      .then(data => {
        if (!cancelled) {
          setRoutes(data);
        }
      })
      .catch(err => {
        if (!cancelled) setError(err.message || "Failed to load routes");
      })
      .finally(() => !cancelled && setLoading(false));

    return () => {
      cancelled = true;
    };
  }, []);

  return (
    <div className="app-shell">
      <RoutesList
        routes={routes}
        loading={loading}
        error={error ?? undefined}
        selectedRouteId={selectedRouteId}
        onSelect={setSelectedRouteId}
      />
      <MapView selectedRouteId={selectedRouteId} />
    </div>
  );
}

export default App;
