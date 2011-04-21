/* Oil and Gas Administragion */
DROP TABLE IF EXISTS `OilAndGasAdministrations`;
CREATE TABLE `OilAndGasAdministrations` (
    `id` INTEGER PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `max_efficiency_deviation` FLOAT
);

INSERT INTO `OilAndGasAdministrations` VALUES (1,'SampleAdministraition', 20);
INSERT INTO `OilAndGasAdministrations` VALUES (2,'OtherAdministraition', 10);

    
/* Pump */
DROP TABLE IF EXISTS `Pumps`;
CREATE TABLE `Pumps` (
    `id` INTEGER PRIMARY KEY,
    `efficiency_deviation_row` TEXT NOT NULL
);

INSERT INTO `Pumps` VALUES (1,'real[0.19,0.19,0.18,0.18,0.20,0.19,0.20,0.19,0.20,0.19,0.19,0.20]');
INSERT INTO `Pumps` VALUES (2,'real[0.27,0.26,0.25,0.25,0.25,0.23,0.22,0.20,0.19,0.18,0.16,0.15]');
INSERT INTO `Pumps` VALUES (3,'real[0.05,0.06,0.08,0.09,0.10,0.12,0.13,0.15,0.15,0.15,0.16,0.17]');
INSERT INTO `Pumps` VALUES (4,'real[0.12,0.11,0.10,0.10,0.10,0.08,0.07,0.05,0.04,0.03,0.01,0.00]');


/*Oil and Gas Administation <-> Pump */
DROP TABLE IF EXISTS `OilAndGasAdministration_Pump`;
CREATE TABLE `OilAndGasAdministration_Pump` (
    `id` INTEGER PRIMARY KEY,
    `oaga_id` INTEGER NOT NULL,
    `pump_id` INTEGER NOT NULL
);

INSERT INTO `OilAndGasAdministration_Pump` VALUES (1,1,1);
INSERT INTO `OilAndGasAdministration_Pump` VALUES (2,1,2);
INSERT INTO `OilAndGasAdministration_Pump` VALUES (3,1,3);
INSERT INTO `OilAndGasAdministration_Pump` VALUES (4,2,4);
