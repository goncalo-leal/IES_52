CREATE DATABASE IF NOT EXISTS `ies_52`;
USE `ies_52`;



CREATE TABLE IF NOT EXISTS `User_State` (
    `id`			INT		AUTO_INCREMENT			NOT NULL,
	`description`		VARCHAR(100)		NOT NULL,
	PRIMARY KEY(`id`)
);



CREATE TABLE IF NOT EXISTS `User` (
    `id`			INT		AUTO_INCREMENT			NOT NULL,
    `state`			INT		NOT NULL,
	`nome`		VARCHAR(100)		NOT NULL,
	`email`		VARCHAR(150)		NOT NULL,
	`password`		VARCHAR(150)		NOT NULL,
	`birtday`		Date		NOT NULL,
	`gender`		VARCHAR(10)		NOT NULL,
	PRIMARY KEY(`id`),
	FOREIGN KEY(`state`) REFERENCES User_State(`id`)
);
GO

CREATE TABLE IF NOT EXISTS `Shopping` (
    `id`			INT		AUTO_INCREMENT			NOT NULL,
	`nome`		VARCHAR(100)		NOT NULL,
	`location`		VARCHAR(500)		NOT NULL,
	`capacity`		INT		NOT NULL,
	`abertura`		time		NOT NULL,
	`fecho`		time		NOT NULL,
	PRIMARY KEY(`id`)
);
GO
CREATE TABLE IF NOT EXISTS `Store` (
    `id`			INT		AUTO_INCREMENT			NOT NULL,
	`nome`		VARCHAR(100)		NOT NULL,
	`location`		VARCHAR(500)		NOT NULL,
	`capacity`		INT		NOT NULL,
	`abertura`		time		NOT NULL,
	`fecho`		time		NOT NULL,
	`id_shopping`		int		NOT NULL,
	PRIMARY KEY(`id`),
	FOREIGN KEY (`id_shopping`) REFERENCES Shopping(`id`)
);
GO
CREATE TABLE IF NOT EXISTS `Product` (
    `id`			INT		AUTO_INCREMENT			NOT NULL,
	`nome`		VARCHAR(100)		NOT NULL,
	`reference`		VARCHAR(500)		NOT NULL,
	`stock`		INT		NOT NULL,
	`price`		float		NOT NULL,
	`description`		varchar(1000)		NOT NULL,
	`id_store`		int		NOT NULL,
	PRIMARY KEY(`id`),
	FOREIGN KEY (`id_store`) REFERENCES Store(`id`)
);
GO
CREATE TABLE IF NOT EXISTS `Park` (
    `id`			INT		AUTO_INCREMENT			NOT NULL,
	`nome`		VARCHAR(100)		NOT NULL,
	`location`		VARCHAR(500)		NOT NULL,
	`capacity`		INT		NOT NULL,
	`abertura`		time		NOT NULL,
	`fecho`		time		NOT NULL,
	`id_shopping`		int		NOT NULL,
	PRIMARY KEY(`id`),
	FOREIGN KEY (`id_shopping`) REFERENCES Shopping(`id`)
);
GO

CREATE TABLE IF NOT EXISTS `Shopping_Manager` (
    `id_user`			INT					NOT NULL,
    `id_shopping`			INT					NOT NULL,
	PRIMARY KEY(`id_user`),
    FOREIGN KEY (`id_shopping`) REFERENCES Shopping(`id`),
	FOREIGN KEY (`id_user`) REFERENCES User(`id`)
);
GO
CREATE TABLE IF NOT EXISTS `Store_Manager` (
    `id_user`			INT					NOT NULL,
    `id_store`			INT					NOT NULL,
	PRIMARY KEY(`id_user`),
    FOREIGN KEY (`id_store`) REFERENCES Store(`id`),
	FOREIGN KEY (`id_user`) REFERENCES User(`id`)
);
GO

CREATE TABLE IF NOT EXISTS `Sensor` (
    `id`			INT		AUTO_INCREMENT			NOT NULL,
	`tipo`		VARCHAR(100)		NOT NULL,
	PRIMARY KEY(`id`)
);
GO


CREATE TABLE IF NOT EXISTS `Sensor_Park` (
    `id_sensor`			INT					NOT NULL,
    `id_park`			INT					NOT NULL,
	PRIMARY KEY(`id_sensor`),
    FOREIGN KEY (`id_sensor`) REFERENCES Sensor(`id`),
	FOREIGN KEY (`id_park`) REFERENCES Park(`id`)
);
GO

CREATE TABLE IF NOT EXISTS `Sensor_Shopping` (
    `id_sensor`			INT					NOT NULL,
    `id_shopping`			INT					NOT NULL,
	PRIMARY KEY(`id_sensor`),
    FOREIGN KEY (`id_sensor`) REFERENCES Sensor(`id`),
	FOREIGN KEY (`id_shopping`) REFERENCES Shopping(`id`)
);
GO

CREATE TABLE IF NOT EXISTS `Sensor_Store` (
    `id_sensor`			INT					NOT NULL,
    `id_store`			INT					NOT NULL,
	PRIMARY KEY(`id_sensor`),
    FOREIGN KEY (`id_sensor`) REFERENCES Sensor(`id`),
	FOREIGN KEY (`id_store`) REFERENCES Store(`id`)
);
GO



CREATE TABLE IF NOT EXISTS `Sensor_Data` (
     `id`			INT		AUTO_INCREMENT			NOT NULL,
	`data`		VARCHAR(1000)		NOT NULL,
    `timestamp` TIMESTAMP NOT NULL,
    `id_sensor`			INT					NOT NULL,
	PRIMARY KEY(`id`),
    FOREIGN KEY (`id_sensor`) REFERENCES Sensor(`id`)

);
GO









