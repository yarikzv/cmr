create table public.place_of_loading
(
    id         int auto_increment not null primary key,
    address    varchar(500),
    country_id int,
    foreign key (country_id) references countries (id)
)