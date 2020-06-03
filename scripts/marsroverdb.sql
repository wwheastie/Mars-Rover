CREATE DATABASE `marsrover`;

USE marsrover;

CREATE TABLE `image` (`photoid` int NOT NULL, `imageblob` longblob NOT NULL, `nasaurl` varchar(200) NOT NULL, `url` varchar(200) NOT NULL, `date` date NOT NULL, `size` int NOT NULL, PRIMARY KEY (`photoid`), UNIQUE KEY `nasaimageurl_UNIQUE` (`nasaurl`), UNIQUE KEY `imageurl_UNIQUE` (`url`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
