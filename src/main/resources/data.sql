/**
 * Author:  Muhammed.Ibrahim
 * Created: Apr 21, 2023
 */
INSERT INTO drones(id, serialNumber, model, weightLimit, batteryCapacity, state) 
    VALUES (1, 'L2B16438200041G7', 'Lightweight', 200.0, 100, 'LOADING'),
            (2, 'B1B16438202041A7', 'Middleweight', 300.0, 97, 'LOADED'),
            (3, 'A7B1643823304197', 'Heavyweight', 500.0, 24, 'IDLE'),
            (4, 'L7A39438200041G9', 'Cruiserweight', 450.0, 100, 'DELIVERING'),
            (5, 'YYB1643820004102', 'Heavyweight', 500.0, 89, 'RETURNING'),
            (6, 'Y2B16038200041G7', 'Lightweight', 200.0, 100, 'DELIVERED'),
            (7, 'A1B16438202041A7', 'Middleweight', 300.0, 97, 'DELIVERED'),
            (8, 'A9Y0643823304197', 'Heavyweight', 500.0, 24, 'IDLE'),
            (9, 'M2B39438200041G9', 'Cruiserweight', 450.0, 100, 'DELIVERING'),
            (10, 'ZBB1643820004102', 'Heavyweight', 500.0, 89, 'LOADING');

INSERT INTO loads(id, droneId, name, weight, code, image)
    VALUES (1, 1, 'Hyperlophene', 50.0, 'MED_51A', 'Base64_Image_String_1'),
            (2, 2, 'Protophone BP', 150.0, 'MED_81C', 'Base64_Image_String_2'),
            (3, 1, 'Deptolopene Htc', 55.0, 'MED2_10', 'Base64_Image_String_3'),
            (4, 1, 'Medicare', 70.0, 'MED_71A', 'Base64_Image_String_4'),
            (5, 2, 'Tetraclophone methane', 100.0, 'MED_88', 'Base64_Image_String_5');
