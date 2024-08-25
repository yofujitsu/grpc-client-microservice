create table data
(
    id         bigserial primary key,
    hero_id    bigint           not null,
    timestamp  timestamp        not null,
    value      double precision not null,
    value_type varchar          not null
);