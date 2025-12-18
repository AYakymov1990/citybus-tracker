create table routes (
  id bigserial primary key,
  name varchar(200) not null,
  city varchar(120) not null,
  active boolean not null default true,
  created_at timestamptz not null default now()
);

create table stops (
  id bigserial primary key,
  route_id bigint not null references routes(id) on delete cascade,
  name varchar(200) not null,
  lat double precision not null,
  lon double precision not null,
  seq int not null
);

create index idx_stops_route_seq on stops(route_id, seq);

create table buses (
  id bigserial primary key,
  route_id bigint not null references routes(id) on delete cascade,
  code varchar(50) not null,
  lat double precision not null,
  lon double precision not null,
  updated_at timestamptz not null default now()
);

create index idx_buses_route on buses(route_id);

create table bookings (
  id bigserial primary key,
  route_id bigint not null references routes(id) on delete cascade,
  passenger_name varchar(200) not null,
  seats int not null check (seats > 0),
  created_at timestamptz not null default now()
);

create index idx_bookings_route on bookings(route_id);
