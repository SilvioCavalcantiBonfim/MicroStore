CREATE TABLE
  IF NOT EXISTS shopping.item (
    shop_id BIGSERIAL REFERENCES shopping.shop (id),
    product_identifier VARCHAR(100) NOT NULL,
    price FLOAT NOT NULL,
    amount INTEGER NOT NULL
  );