/**
 * Author:  Muhammed.Ibrahim
 * Created: Apr 21, 2023
 */
CREATE TABLE IF NOT EXISTS drones (
    id INTEGER NOT NULL AUTO_INCREMENT,
    serialNumber VARCHAR(100) NOT NULL,
    model ENUM('Lightweight','Middleweight','Cruiserweight','Heavyweight') NOT NULL,
    weightLimit DECIMAL(5,2) NOT NULL,
    batteryCapacity INTEGER NOT NULL,
    state ENUM('IDLE','LOADING','LOADED','DELIVERING','DELIVERED','RETURNING') NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS loads (
    id INTEGER NOT NULL AUTO_INCREMENT,
    droneId INTEGER NOT NULL,
    name VARCHAR(128) NOT NULL,
    weight DECIMAL(5,2) NOT NULL, 
    code VARCHAR(128) NOT NULL,
    image TEXT,
    PRIMARY KEY (id)
);

INSERT INTO drones(serialNumber, model, weightLimit, batteryCapacity, state) 
    VALUES ('L2B16438200041G7', 'Lightweight', 200.0, 100, 'LOADING'),
            ('B1B16438202041A7', 'Middleweight', 300.0, 97, 'LOADED'),
            ('A7B1643823304197', 'Heavyweight', 500.0, 24, 'IDLE'),
            ('L7A39438200041G9', 'Cruiserweight', 450.0, 100, 'DELIVERING'),
            ('YYB1643820004102', 'Heavyweight', 500.0, 89, 'RETURNING'),
            ('Y2B16038200041G7', 'Lightweight', 200.0, 100, 'DELIVERED'),
            ('A1B16438202041A7', 'Middleweight', 300.0, 97, 'DELIVERED'),
            ('A9Y0643823304197', 'Heavyweight', 500.0, 24, 'IDLE'),
            ('M2B39438200041G9', 'Cruiserweight', 450.0, 100, 'DELIVERING'),
            ('ZBB1643820004102', 'Heavyweight', 500.0, 89, 'LOADING');

INSERT INTO loads(droneId, name, weight, code, image) 
    VALUES (1, 'Hyperlophene', 50.0, 'MED_51A', 'Base64_Image_String_1'),
            (2, 'Protophone BP', 150.0, 'MED_81C', 'Base64_Image_String_2'),
            (1, 'Deptolopene Htc', 55.0, 'MED2_10', 'Base64_Image_String_3'),
            (1, 'Medicare', 70.0, 'MED_71A', 'Base64_Image_String_4'),
            (2, 'Tetraclophone methane', 100.0, 'MED_88', 'Base64_Image_String_5');
