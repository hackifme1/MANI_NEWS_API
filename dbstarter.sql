CREATE TABLE api_call_records (
id SERIAL PRIMARY KEY,
endpoint_name character varying(255),
response text,
call_time timestamp without time zone,
response_time bigint,
request character varying(255),
user_id uuid REFERENCES users(id)
);

CREATE TABLE users (
id uuid PRIMARY KEY,
username character varying(255),
email character varying(255),
selected_country character varying(255),
selected_category character varying(255),
is_subscribed boolean NOT NULL DEFAULT false
);

CREATE TABLE endpoint_metrics (
endpoint_name character varying(255) PRIMARY KEY,
avg_response_time double precision,
p99_time bigint,
num_calls bigint
);

CREATE TABLE news_sources (
id character varying(255) PRIMARY KEY,
name character varying(255),
description character varying(255),
url character varying(255),
category character varying(255),
language character varying(255),
country character varying(255)
);

CREATE TABLE user_news_sources (
user_id uuid NOT NULL REFERENCES users(id),
news_source_id character varying(255) NOT NULL REFERENCES news_sources(id)
);