create sequence order_id_seq start 1 increment 1;

create table if not exists "orders"
(
    id               bigint         not null,
    product_name     text           not null,
    product_category text           not null,
    quantity         smallint       not null,
    price            decimal(15, 2) not null,
    order_at         timestamp      not null,
    primary key (id)
);