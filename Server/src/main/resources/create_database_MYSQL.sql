CREATE DATABASE VVSSFestival;

CREATE TABLE `users` (
    `Username` varchar(45) NOT NULL,
    `Password` varchar(45) NOT NULL,
    PRIMARY KEY (`Username`)
);

CREATE TABLE `artists` (
    `numeArtist` varchar(45) NOT NULL,
    `genMuzical` varchar(255) DEFAULT NULL,
    `companie` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`numeArtist`)
);

CREATE TABLE `concerts` (
    `idConcert` int(11) NOT NULL AUTO_INCREMENT,
    `Artist` varchar(45) NOT NULL,
    `Locatie` varchar(45) NOT NULL,
    `Data` date NOT NULL,
    `Ora` time NOT NULL,
    `BileteTotale` int(11) NOT NULL,
    `BileteVandute` int(11) NOT NULL,
    PRIMARY KEY (`idConcert`),
    KEY `concerts_artists_idx` (`Artist`),
    CONSTRAINT `concerts_artists` FOREIGN KEY (`Artist`) REFERENCES `artists` (`numeArtist`)
);

CREATE TABLE `transactions` (
    `idTranzactie` int(11) NOT NULL AUTO_INCREMENT,
    `UsernameVanzator` varchar(45) DEFAULT NULL,
    `Cumparator` varchar(45) NOT NULL,
    `idConcert` int(11) DEFAULT NULL,
    `TicheteCumparate` int(11) NOT NULL,
    PRIMARY KEY (`idTranzactie`),
    KEY `transactions_concert_idx` (`idConcert`),
    KEY `transactions_users_idx` (`UsernameVanzator`),
    CONSTRAINT `transactions_concert` FOREIGN KEY (`idConcert`) REFERENCES `concerts` (`idConcert`),
    CONSTRAINT `transactions_users` FOREIGN KEY (`UsernameVanzator`) REFERENCES `users` (`Username`)
);