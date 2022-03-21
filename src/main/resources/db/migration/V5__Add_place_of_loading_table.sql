create table public.place_of_loading
(
    id         int auto_increment not null primary key,
    address    varchar(500) not null ,
    country_id int not null ,
    foreign key (country_id) references countries (id)
)