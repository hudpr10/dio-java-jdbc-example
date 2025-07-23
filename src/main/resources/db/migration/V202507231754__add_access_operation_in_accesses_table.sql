ALTER TABLE accesses
    ADD COLUMN read_data BOOLEAN DEFAULT false,
    ADD COLUMN create_data BOOLEAN DEFAULT false,
    ADD COLUMN update_data BOOLEAN DEFAULT false,
    ADD COLUMN delete_data BOOLEAN DEFAULT false;