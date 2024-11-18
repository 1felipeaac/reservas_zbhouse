ALTER TABLE reservas
ADD ativo BOOLEAN NOT NULL DEFAULT true;

UPDATE reservas
SET ativo = true;