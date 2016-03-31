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


steps to try out:

git clone https://github.com/prabhuinbarajan/storm_ksk.git
cd storm_ksk
# adjust the configuration file truck_event_topology.properties in src/main/resources
cqlsh < src/main/resources/truck_event.cql

#build the package

mvn clean package

#submit to storm.
storm jar target/ksk-1.0-SNAPSHOT.jar com.hippocamp.ksk.storm.TruckEventProcessingTopology truck-topology remote -c nimbus.host=<your host>

#in a separate terminal start pumping events to kafka:

cd storm_ksk
java -cp target/ksk-1.0-SNAPSHOT.jar com.hippocamp.ksk.producer.TruckEventsProducer kafka.default:9092 zookeeper.default:2181 &

check the log file for errors/ adjust src/main/log4j.properties for log level

check cassandra for events showing up
select * from truck_events;

