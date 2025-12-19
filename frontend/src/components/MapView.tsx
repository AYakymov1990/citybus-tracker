import { useEffect, useMemo, useState } from "react";
import { MapContainer, Marker, Polyline, Popup, TileLayer, useMap } from "react-leaflet";
import type { BusPosition, Stop } from "../api/types";
import { getBusPositions, getStops } from "../api/client";
import type { LatLngExpression } from "leaflet";
import L from "leaflet";

type MapViewProps = {
  selectedRouteId: number | null;
};

const DEFAULT_CENTER: LatLngExpression = [51.7592, 19.455];

export function MapView({ selectedRouteId }: MapViewProps) {
  const [stops, setStops] = useState<Stop[]>([]);
  const [bus, setBus] = useState<BusPosition | null>(null);
  const [status, setStatus] = useState("Select a route to see details");
  const [error, setError] = useState<string | null>(null);
  const [loadingStops, setLoadingStops] = useState(false);

  useEffect(() => {
    if (!selectedRouteId) {
      setStops([]);
      setBus(null);
      setStatus("Select a route to see details");
      setError(null);
      return;
    }

    let cancelled = false;
    setLoadingStops(true);
    setStatus("Loading stops...");
    setError(null);
    setStops([]);
    setBus(null);

    getStops(selectedRouteId)
      .then(data => {
        if (cancelled) return;
        setStops(data);
        setStatus(data.length ? `Route has ${data.length} stops.` : "Route has no stops yet.");
      })
      .catch(err => {
        if (cancelled) return;
        setError(err.message || "Failed to load stops");
        setStops([]);
        setStatus("Unable to load stops.");
      })
      .finally(() => !cancelled && setLoadingStops(false));

    return () => {
      cancelled = true;
    };
  }, [selectedRouteId]);

  useEffect(() => {
    if (!selectedRouteId) {
      return;
    }
    let cancelled = false;
    let intervalId: number;

    const fetchBus = () => {
      getBusPositions(selectedRouteId)
        .then(data => {
          if (!cancelled) {
            setBus(data[0] ?? null);
          }
        })
        .catch(() => {
          if (!cancelled) setBus(null);
        });
    };

    fetchBus();
    intervalId = window.setInterval(fetchBus, 2000);

    return () => {
      cancelled = true;
      window.clearInterval(intervalId);
    };
  }, [selectedRouteId]);

  const positions = useMemo(() => stops.map(stop => [stop.lat, stop.lon] as LatLngExpression), [stops]);
  const busPosition = bus ? ([bus.lat, bus.lon] as LatLngExpression) : null;

  return (
    <section className="map-container">
      <MapContainer
        center={DEFAULT_CENTER}
        zoom={12}
        style={{ height: "100%", width: "100%" }}
        scrollWheelZoom
      >
        <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" attribution="&copy; OpenStreetMap contributors" />
        {positions.length > 0 && <Polyline positions={positions} color="#2563eb" weight={4} opacity={0.7} />}
        {stops.map(stop => (
          <Marker key={stop.id} position={[stop.lat, stop.lon]}>
            <Popup>
              <strong>{stop.name}</strong>
              <br />
              Seq: {stop.seq}
            </Popup>
          </Marker>
        ))}
        {busPosition && (
          <Marker position={busPosition}>
            <Popup>
              <strong>{bus?.code}</strong>
              <br />
              Updated: {bus?.updatedAt}
            </Popup>
          </Marker>
        )}
        <FitBounds positions={busPosition ? [...positions, busPosition] : positions} />
      </MapContainer>
      {(status || error || loadingStops) && (
        <div className="status-banner">
          {loadingStops ? "Loading..." : error ? error : status}
        </div>
      )}
    </section>
  );
}

type FitBoundsProps = {
  positions: LatLngExpression[];
};

function FitBounds({ positions }: FitBoundsProps) {
  const map = useMap();
  useEffect(() => {
    if (!positions.length) return;
    const bounds = L.latLngBounds(positions);
    map.fitBounds(bounds, { padding: [40, 40] });
  }, [positions, map]);
  return null;
}
