CREATE TABLE
  IF NOT EXISTS shopping.shop (
    id BIGSERIAL PRIMARY KEY,
    user_identifier VARCHAR(100) NOT NULL,
    date TIMESTAMP NOT NULL,
    total FLOAT NOT NULL
  );