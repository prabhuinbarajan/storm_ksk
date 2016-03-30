# storm_kafka_tutorial

Implemented a simple Kafka + STORM + Cassandra Pipeline.
checkout tutorial 2

inspired from jkpchang and tjake's storm scraper.
https://github.com/jkpchang/storm_kafka_tutorial
https://github.com/tjake/stormscraper



steps to try out:

git clone https://github.com/prabhuinbarajan/storm_kafka_tutorial.git
cd storm_kafka_tutorial
# adjust the configuration file truck_event_topology.properties in src/main/resources
cqlsh < src/main/resources/truck_event.cql

#build the package

mvn clean package
#submit to storm.
storm jar target/Tutorial-1.0-SNAPSHOT.jar com.hortonworks.tutorials.tutorial2.TruckEventProcessingTopology truck-topology remote -c nimbus.host=<your host>

#in a separate terminal start pumping events to kafka:

cd storm_kafka_tutorial
java -cp target/Tutorial-1.0-SNAPSHOT.jar com.hortonworks.tutorials.tutorial1.TruckEventsProducer kafka.default:9092 zookeeper.default:2181 &

check the log file for errors/ adjust src/main/log4j.properties for log level

check cassandra for events

