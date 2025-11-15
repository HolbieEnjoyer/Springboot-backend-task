create table employees (
    id serial primary key,
    name varchar(30) not null unique,
    email varchar(20) not null unique,
    password varchar(25) not null,
    role varchar(10) not null,
    created_at timestamp default current_timestamp
);