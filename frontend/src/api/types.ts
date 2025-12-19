export type Route = {
  id: number;
  name: string;
  city: string;
  active: boolean;
};

export type Stop = {
  id: number;
  name: string;
  lat: number;
  lon: number;
  seq: number;
};

export type BusPosition = {
  id: number;
  code: string;
  lat: number;
  lon: number;
  updatedAt: string;
};
