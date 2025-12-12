-- Run as a superuser: psql -U postgres -f src/main/resources/init_db.sql
-- psql -U username -d database_name -h hostname -p port
-- psql "postgresql://eventuser:change_me@127.0.0.1:5432/eventdb"

\set dbname eventdb
\set dbuser eventuser
\set dbpass
\set ON_ERROR_STOP on

-- Create database if missing (Postgres 16+)
create database :"dbname" if not exists;

-- Create app user if missing
select 'create user ' || quote_ident(:'dbuser') || ' with password ' || quote_literal(:'dbpass')
where not exists (select from pg_roles where rolname = :'dbuser')\gexec

grant all privileges on database :"dbname" to :"dbuser";

-- Switch to target DB
\connect :"dbname"

create table if not exists events (
    id bigserial primary key,
    title text,
    detail_url text,
    date_time timestamp,
    duration text,
    location text,
    price text,
    ticket_url text,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    constraint uq_events_detail unique (detail_url)
);

create index if not exists idx_events_date_time on events(date_time);

-- Make the app user own the table
alter table events owner to :"dbuser";
