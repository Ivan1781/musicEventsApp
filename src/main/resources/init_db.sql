\if :{?dbname}
\else
  \echo 'FATAL: dbname is required (pass via -v dbname=...)'
  \quit 1
\endif

\if :{?dbuser}
\else
  \echo 'FATAL: dbuser is required (pass via -v dbuser=...)'
  \quit 1
\endif

\if :{?dbpass}
\else
  \echo 'FATAL: dbpass is required (pass via -v dbpass=...)'
  \quit 1
\endif

\set ON_ERROR_STOP on

select 'create database ' || quote_ident(:'dbname')
where not exists (select from pg_database where datname = :'dbname')\gexec

select 'create user ' || quote_ident(:'dbuser') || ' with password ' || quote_literal(:'dbpass')
where not exists (select from pg_roles where rolname = :'dbuser')\gexec

grant all privileges on database :"dbname" to :"dbuser";

\connect :"dbname"

create table if not exists events (
    id bigserial primary key,
    title text,
    city text,
    detail_url text,
    date_time timestamp,
    duration text,
    location text,
    price text,
    ticket_url text,
    category text,
    author text,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    constraint uq_events_detail_datetime unique (detail_url, date_time)
);

create index if not exists idx_events_date_time on events(date_time);

alter table events owner to :"dbuser";
