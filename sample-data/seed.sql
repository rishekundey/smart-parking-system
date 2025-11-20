-- Sample seed data for parking spots and floors
INSERT INTO floors (id, name) VALUES (1, 'Ground'), (2, 'First'), (3, 'Second');
-- sample spots
INSERT INTO parking_spots (id, floor_id, size, occupied, maintenance) VALUES
('F1-S1',1,'MINI',false,false),('F1-S2',1,'COMPACT',false,false),('F1-S3',1,'LARGE',false,false);
