CREATE KEYSPACE testkeyspace WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '1'
};
CREATE TABLE TRUCK_EVENT (
	driverId text ,
	truckId text ,
	eventTime timestamp,
	eventType text,
	longitude text, 
	latitude text,
	PRIMARY KEY (driverId, eventTime)
);
