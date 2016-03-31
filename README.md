# storm_kafka_tutorial

Implemented a simple Kafka + STORM + Cassandra Pipeline.


inspired from jkpchang and tjake's storm scraper.
https://github.com/jkpchang/storm_kafka_tutorial
https://github.com/tjake/stormscraper

Pre Requisites:
storm 0.9.3 
cassandra 2.13
Kafka 0.9 + 

- refer to : https://github.com/prabhuinbarajan/blaze-analytics for installation of these artifacts on kubernetes



# checkout code
git clone https://github.com/prabhuinbarajan/storm_ksk.git
cd storm_ksk

# adjust the configuration file 
change truck_event_topology.properties in src/main/resources
# add schema to cassandra
cqlsh < src/main/resources/truck_event.cql

#build the package

mvn clean package

#submit to storm.
storm jar target/ksk-1.0-SNAPSHOT.jar com.hippocamp.ksk.storm.TruckEventProcessingTopology truck-topology remote -c nimbus.host=<your host>
(you can also run local storm - checkout TruckEventProcessingTopology)

you will see something like this 
```
2998 [main] INFO  backtype.storm.StormSubmitter - Submitting topology truck-event-processor in distributed mode with conf {"hbase.persist.all.events":"false","writer.cql":"INSERT INTO TRUCK_EVENT (driverId,truckId, eventTime, eventType, longitude, latitude) values (?,?,?,?,?,?)","nimbus.host":"nimbus.internal.hippocamp.io","hdfs.file.rotation.time.minutes":"5","hive.staging.table.name":"truck_events_text_partition","spout.thread.count":"1","kafka.zookeeper.host.port":"zooc.default:2181","topology.debug":true,"hdfs.url":"hdfs:\/\/hadoop-m.c.onefold-1.internal:8020","cassandra.nodes":"cassandra.default","cassandra.keyspace":"testkeyspace","hive.metastore.url":"thrift:\/\/hadoop-w-0.c.onefold-1.internal:9083","hive.database.name":"default","hdfs.path":"\/truck-events-v4","kafka.zkRoot":"\/truck_event_sprout","kafka.topic":"truckevent","hdfs.file.prefix":"truckEvents","logTruckEventBolt.cql":"INSERT INTO TRUCK_EVENT (driverId,truckId, eventTime, eventType, longitude, latitude) values (?,?,?,?,?,?)"}
3056 [main] INFO  backtype.storm.StormSubmitter - Finished submitting topology: truck-event-processor
```

# kafka producer
in a separate terminal start pumping events to kafka:

cd storm_ksk
java -cp target/ksk-1.0-SNAPSHOT.jar com.hippocamp.ksk.producer.TruckEventsProducer kafka.default:9092 zookeeper.default:2181 &

#observe
check the log file for errors/ adjust src/main/log4j.properties for log level

check cassandra for events showing up
select * from truck_events;

