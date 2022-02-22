create table public.passport
(
    id     int auto_increment not null primary key,
    number varchar(20),
    date   date,
    issue  varchar(255)
);

create table public.drivers
(
    id          int auto_increment not null primary key,
    lastname    varchar(25)        not null,
    middlename  varchar(25),
    firstname   varchar(25),
    passport_id int,
    truck       varchar(12),
    trailer     varchar(12),
    foreign key (passport_id) references passport (id)
)
