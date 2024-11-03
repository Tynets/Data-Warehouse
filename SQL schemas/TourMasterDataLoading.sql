USE TourMaster;
GO

BULK INSERT Client FROM 'D:\Data\Clients.bulk' WITH (FIELDTERMINATOR = '|', ROWTERMINATOR = '0x0A');
GO

BULK INSERT Hotel FROM 'D:\Data\Hotels.bulk' WITH (FIELDTERMINATOR = '|', ROWTERMINATOR = '0x0A');
GO

BULK INSERT Transporter FROM 'D:\Data\Transporters.bulk' WITH (FIELDTERMINATOR = '|', ROWTERMINATOR = '0x0A');
GO

BULK INSERT Tour FROM 'D:\Data\Tours.bulk' WITH (FIELDTERMINATOR = '|', ROWTERMINATOR = '0x0A');
GO

BULK INSERT RoomReservation FROM 'D:\Data\Reservations.bulk' WITH (FIELDTERMINATOR = '|', ROWTERMINATOR = '0x0A');
GO

BULK INSERT Transportation FROM 'D:\Data\Transportations.bulk' WITH (FIELDTERMINATOR = '|', ROWTERMINATOR = '0x0A');
GO

BULK INSERT Tourist FROM 'D:\Data\Tourists.bulk' WITH (FIELDTERMINATOR = '|', ROWTERMINATOR = '0x0A');
GO
