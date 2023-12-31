CREATE TABLE IF NOT EXISTS
products.product (
  id BIGSERIAL PRIMARY KEY,
  product_identifier VARCHAR NOT NULL,
  name VARCHAR(100) NOT NULL,
  description VARCHAR NOT NULL,
  price FLOAT NOT NULL,
  category_id BIGINT REFERENCES products.category(id)
);