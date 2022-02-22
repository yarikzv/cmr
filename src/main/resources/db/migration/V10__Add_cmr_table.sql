create table public.cmr
(
    id                   int auto_increment not null primary key,
    number               varchar(11),
    date                 date,
    sender_id            int,
    recipient_id         int,
    place_of_delivery_id int,
    place_of_loading_id  int,
    documents            varchar(500),
    container_id         int,
    cargo_name           varchar(500),
    cargo_quantity       int,
    cargo_weight         varchar(10),
    cargo_code           bigint,
    place_of_issue       varchar(100),
    driver_id            int,
    foreign key (sender_id) references public.sender (id),
    foreign key (recipient_id) references public.recipient (id),
    foreign key (place_of_delivery_id) references public.place_of_delivery (id),
    foreign key (place_of_loading_id) references public.place_of_loading (id),
    foreign key (container_id) references public.container (id),
    foreign key (driver_id) references public.drivers (id)
)