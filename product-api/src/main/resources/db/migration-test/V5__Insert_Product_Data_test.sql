-- Inserindo registros na tabela product
INSERT INTO
  products.product (
    product_identifier,
    name,
    description,
    price,
    category_id
  )
VALUES
  (
    'ABC123',
    'Smartphone Modelo X',
    'Um smartphone avançado com ótimas funcionalidades.',
    799.99,
    1
  );