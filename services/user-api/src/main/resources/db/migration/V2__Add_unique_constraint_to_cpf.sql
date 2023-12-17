ALTER TABLE users.user
ADD CONSTRAINT cpf_unique UNIQUE (cpf),
ADD CONSTRAINT cpf_length CHECK (LENGTH(cpf) = 11);