ALTER TABLE users
  ADD COLUMN password TEXT NOT NULL,
  ALTER COLUMN name TYPE TEXT;

INSERT INTO users(id, name, password) VALUES(
  '2c0902c8-2314-41a1-8351-5f74eb473f96',
  'Pavel',
  '$argon2i$v=19$m=65536,t=10,p=1$Qsosaf8PbIClgnDCdoR+6g$R3UzjZTB42pTA8zN/SL1bd7l9nDhFluBZaAhxnnuKS8'
)
