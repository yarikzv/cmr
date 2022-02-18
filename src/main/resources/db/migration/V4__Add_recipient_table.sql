create table public.recipient
(
    id         serial       not null primary key,
    name       varchar(255) not null,
    address    varchar(500),
    country_id int,
    foreign key (country_id) references countries (id)
)