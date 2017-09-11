-- Database definition

-- Tables

CREATE TABLE IF NOT EXISTS public.settings
(
  id INTEGER PRIMARY KEY,
  location TEXT,
  tv_times TEXT
)
ALTER TABLE public.settings OWNER TO ecd;

CREATE TABLE IF NOT EXISTS public.signage_stream
(
  id UUID PRIMARY KEY,
  name text UNIQUE NOT NULL,
  timing INTEGER,
  frames TEXT,
  creation_date_time TIMESTAMP WIHOUT TIME ZONE,
  last_update_date_time TIMESTAMP WIHOUT TIME ZONE
)
ALTER TABLE public.settings OWNER TO ecd;

CREATE TABLE IF NOT EXISTS public.person
(
  id SERIAL PRIMARY KEY,
  name TEXT
)
ALTER TABLE public.person OWNER TO ecd;
