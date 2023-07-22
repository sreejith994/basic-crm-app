ALTER TABLE customer
ADD CONSTRAINT chk_gender CHECK (LOWER(gender) IN ('male', 'female'));