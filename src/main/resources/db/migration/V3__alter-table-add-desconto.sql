ALTER TABLE reservas ADD COLUMN desconto INTEGER DEFAULT 0 NOT NULL
CHECK (desconto >= 0 AND desconto <= 100);