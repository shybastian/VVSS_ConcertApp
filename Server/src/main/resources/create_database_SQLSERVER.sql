CREATE DATABASE VVSSFestival;

CREATE TABLE users (
    Username varchar(45) NOT NULL,
    Password varchar(45) NOT NULL,
    primary key (Username)
);

CREATE TABLE artists (
    numeArtist varchar(45) NOT NULL,
    genMuzical varchar(255) DEFAULT NULL,
    companie varchar(255) DEFAULT NULL,
    PRIMARY KEY (numeArtist)
);

CREATE TABLE concerts (
    idConcert int NOT NULL IDENTITY(1,1),
    Artist varchar(45) NOT NULL,
    Locatie varchar(45) NOT NULL,
    Data date NOT NULL,
    Ora time NOT NULL,
    BileteTotale int NOT NULL,
    BileteVandute int NOT NULL,
    PRIMARY KEY (idConcert),
    CONSTRAINT concerts_artists FOREIGN KEY (Artist) REFERENCES artists(numeArtist)
);

CREATE TABLE transactions (
    idTranzactie int NOT NULL IDENTITY(1,1),
    UsernameVanzator varchar(45) DEFAULT NULL,
    Cumparator varchar(45) NOT NULL,
    idConcert int DEFAULT NULL,
    TicheteCumparate int NOT NULL,
    PRIMARY KEY (idTranzactie),
    CONSTRAINT transactions_concerts FOREIGN KEY (idConcert) REFERENCES concerts(idConcert),
    CONSTRAINT transactions_users FOREIGN KEY (UsernameVanzator) REFERENCES users(Username),
);
