CREATE TABLE ecd_app.settings
(
  id integer PRIMARY KEY,
  location text,
  tv_times text
);

CREATE TABLE ecd_app.signage_stream
(
  id uuid PRIMARY KEY,
  name text NOT NULL,
  timing integer,
  frames json,
  creation_date_time timestamp without time zone,
  last_update_date_time timestamp without time zone
);

CREATE TABLE ecd_app.news_feed
(
  id uuid PRIMARY KEY,
  name text,
  url text
);

CREATE TABLE ecd_app.playlist
(
  id uuid PRIMARY KEY,
  stream_ids UUID[],
  news_feed_ids UUID[]
);

CREATE TABLE ecd_app.person
(
  id SERIAL PRIMARY KEY,
  name text
);
