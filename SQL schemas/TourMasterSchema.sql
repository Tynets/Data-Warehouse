CREATE DATABASE TourMaster;
GO

USE TourMaster;
GO

CREATE TABLE Client (
	ClientID NUMERIC (9, 0) PRIMARY KEY CHECK (ClientID > 0),
	FirstName VARCHAR (20) NOT NULL,
	LastName VARCHAR (20) NOT NULL,
	DateOfBirth DATE NOT NULL,
	PhoneNumber VARCHAR (15) NOT NULL,
);

CREATE TABLE Hotel (
	HotelID NUMERIC (9, 0) PRIMARY KEY CHECK (HotelID > 0),
	HotelName VARCHAR (70) NOT NULL,
	Rating TINYINT CHECK (Rating > = 1 AND Rating < =  5) NOT NULL,
	Country VARCHAR (20) NOT NULL,
	City VARCHAR (20) NOT NULL,
	HotelAddress VARCHAR (50) NOT NULL,
);

CREATE TABLE Transporter (
	TransporterID NUMERIC (9, 0) PRIMARY KEY CHECK (TransporterID > 0),
	TransporterName VARCHAR (70) NOT NULL,
	Country VARCHAR (20) NOT NULL,
	TransportationType VARCHAR (10) CHECK (TransportationType IN ('Plane', 'Bus')) NOT NULL,
);

CREATE TABLE Tour (
	TourID NUMERIC (9, 0) PRIMARY KEY CHECK (TourID > 0),
	TourtTitle VARCHAR (100) NOT NULL,
	StartDate DATE NOT NULL,
	EndDate DATE NOT NULL,
	Country VARCHAR (20) NOT NULL,
	City VARCHAR (20) NOT NULL,
	Price NUMERIC (10, 2) CHECK (Price > 0.0) NOT NULL,
	TourStatus VARCHAR (20) CHECK (TourStatus IN ('New', 'Recruitment', 'In progress', 'Finished', 'Canceled')) NOT NULL,
	MaxNumOfParticipants TINYINT CHECK (MaxNumOfParticipants > 0) NOT NULL,
	HotelID NUMERIC (9, 0) REFERENCES Hotel (HotelID) NOT NULL,
);

CREATE TABLE RoomReservation (
	ReservationID NUMERIC (9, 0) PRIMARY KEY CHECK (ReservationID > 0),
	RoomNumber NUMERIC(3, 0) CHECK (RoomNumber > 0) NOT NULL,
	NumOfPeople TINYINT CHECK (NumOfPeople > =  1 AND NumOfPeople < =  4) NOT NULL,
	StartDate DATE NOT NULL,
	EndDate DATE NOT NULL,
	Price NUMERIC (10, 2) CHECK (Price > 0.0) NOT NULL,
	HotelID NUMERIC (9, 0) REFERENCES Hotel (HotelID) NOT NULL,
);

CREATE TABLE Transportation (
	Transportation NUMERIC (9, 0) PRIMARY KEY CHECK (Transportation > 0),
	StartDate DATE NOT NULL,
	EndDate DATE NOT NULL,
	Price NUMERIC (10, 2) CHECK (Price > 0.0) NOT NULL,
	TransporterID NUMERIC (9, 0) REFERENCES Transporter (TransporterID) NOT NULL,
	TourID NUMERIC (9, 0) REFERENCES Tour (TourID) NOT NULL,
);

CREATE TABLE Tourist (
	TourID NUMERIC (9, 0) REFERENCES Tour (TourID) NOT NULL,
	ClientID NUMERIC (9, 0) REFERENCES Client (ClientID) NOT NULL,
	TouristStatus VARCHAR (15) CHECK (TouristStatus IN ('Recruited', 'In progress', 'Finished', 'Cancelled')) NOT NULL,
	RoomReservationID NUMERIC (9, 0) REFERENCES RoomReservation (ReservationID) NOT NULL,
	CONSTRAINT TouristPK PRIMARY KEY (TourID, ClientID),
);