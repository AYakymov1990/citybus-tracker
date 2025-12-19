import type { BusPosition, Route, Stop } from "./types";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      "Content-Type": "application/json"
    },
    ...init
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || response.statusText);
  }

  return response.json() as Promise<T>;
}

export function getRoutes(): Promise<Route[]> {
  return request<Route[]>("/api/routes");
}

export function getStops(routeId: number): Promise<Stop[]> {
  return request<Stop[]>(`/api/routes/${routeId}/stops`);
}

export function getBusPositions(routeId: number): Promise<BusPosition[]> {
  return request<BusPosition[]>(`/api/buses/positions?routeId=${routeId}`);
}
