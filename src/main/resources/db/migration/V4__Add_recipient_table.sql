create table public.recipient
(
    id         int auto_increment not null primary key,
    name       varchar(255)       not null,
    edrpou     varchar(8)         not null,
    address    varchar(500),
    country_id int                not null,
    foreign key (country_id) references countries (id)
)