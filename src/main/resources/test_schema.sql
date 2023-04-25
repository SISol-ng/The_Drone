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