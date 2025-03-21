CREATE TABLE inventory
(
    product_id       UUID PRIMARY KEY,
    stock_quantity   INT            NOT NULL,
    price            DECIMAL(10, 2) NOT NULL,
    available        BOOLEAN        NOT NULL,
    category         VARCHAR(255),
    supplier_name    VARCHAR(255),
    manufacture_date DATE
);
