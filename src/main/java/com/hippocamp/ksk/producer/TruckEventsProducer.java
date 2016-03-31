package com.hippocamp.ksk.producer;

/**
 * TruckEventsProducer class simulates the real time truck event generation.
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.google.common.io.Resources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


import org.apache.log4j.Logger;

public class TruckEventsProducer {

    private static final Logger LOG = Logger.getLogger(TruckEventsProducer.class);

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException,
            URISyntaxException {
        if (args.length != 2) {

            System.out.println("Usage: TruckEventsProducer <broker list> <zookeeper>");
            System.exit(-1);
        }

        LOG.debug("Using broker list:" + args[0] + ", zk conn:" + args[1]);

        // long events = Long.parseLong(args[0]);
        Random rnd = new Random();

    /*
    Properties props = new Properties();
    props.put("metadata.broker.list", args[0]);
    props.put("zk.connect", args[1]);
    props.put("serializer.class", "kafka.serializer.StringEncoder");
    props.put("request.required.acks", "1");
    */
        String TOPIC = "truckevent";
        KafkaProducer<String, String> producer = null;
        try {
            InputStream props = Resources.getResource("producer.props").openStream();
            Properties properties = new Properties();
            properties.load(props);
            producer = new KafkaProducer<String, String>(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }


        String[] events = {"Normal", "Normal", "Normal", "Normal", "Normal", "Normal", "Lane Departure", "Overspeed",
                "Normal", "Normal", "Normal", "Normal", "Lane Departure", "Normal", "Normal", "Normal", "Normal",
                "Unsafe tail distance", "Normal", "Normal", "Unsafe following distance", "Normal", "Normal", "Normal",
                "Normal", "Normal", "Normal", "Normal", "Normal", "Normal", "Normal", "Normal", "Normal", "Normal", "Normal",
                "Overspeed", "Normal", "Normal", "Normal", "Normal", "Normal", "Normal", "Normal"};

        Random random = new Random();

        String finalEvent = "";

        String route17 = "route17.kml";
        String[] arrayroute17 = GetKmlLanLangList(route17);
        String route17k = "route17k.kml";
        String[] arrayroute17k = GetKmlLanLangList(route17k);
        String route208 = "route208.kml";
        String[] arrayroute208 = GetKmlLanLangList(route208);
        String route27 = "route27.kml";
        String[] arrayroute27 = GetKmlLanLangList(route27);

        String[] truckIds = {"1", "2", "3", "4"};
        String[] routeName = {"route17", "route17k", "route208", "route27"};
        String[] driverIds = {"11", "12", "13", "14"};

        int evtCnt = events.length;

        // Find max route arraysize.
        int maxarraysize = arrayroute17.length;
        if (maxarraysize < arrayroute17k.length)
            maxarraysize = arrayroute17k.length;
        if (maxarraysize < arrayroute208.length)
            maxarraysize = arrayroute208.length;
        if (maxarraysize < arrayroute27.length)
            maxarraysize = arrayroute208.length;

        for (int i = 0; i < maxarraysize; i++) {

            if (arrayroute17.length > i) {
                finalEvent = new Timestamp(new Date().getTime()) + "|" + truckIds[0] + "|" + driverIds[0] + "|"
                        + events[random.nextInt(evtCnt)] + "|" + getLatLong(arrayroute17[i]);
                try {
                    LOG.info("Sending Messge #: " + routeName[0] + ": " + i + ", msg:" + finalEvent);
                    producer.send(new ProducerRecord<String, String>(
                            TOPIC,
                            finalEvent));
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (arrayroute17k.length > i) {
                finalEvent = new Timestamp(new Date().getTime()) + "|" + truckIds[1] + "|" + driverIds[1] + "|"
                        + events[random.nextInt(evtCnt)] + "|" + getLatLong(arrayroute17k[i]);
                try {
                    LOG.info("Sending Messge #: " + routeName[1] + ": " + i + ", msg:" + finalEvent);
                    producer.send(new ProducerRecord<String, String>(
                            TOPIC,
                            finalEvent));
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (arrayroute208.length > i) {
                finalEvent = new Timestamp(new Date().getTime()) + "|" + truckIds[2] + "|" + driverIds[2] + "|"
                        + events[random.nextInt(evtCnt)] + "|" + getLatLong(arrayroute208[i]);
                try {
                    LOG.info("Sending Messge #: " + routeName[2] + ": " + i + ", msg:" + finalEvent);
                    producer.send(new ProducerRecord<String, String>(
                            TOPIC,
                            finalEvent));
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (arrayroute27.length > i) {
                finalEvent = new Timestamp(new Date().getTime()) + "|" + truckIds[3] + "|" + driverIds[3] + "|"
                        + events[random.nextInt(evtCnt)] + "|" + getLatLong(arrayroute27[i]);
                try {
                    LOG.info("Sending Messge #: " + routeName[3] + ": " + i + ", msg:" + finalEvent);
                    producer.send(new ProducerRecord<String, String>(
                            TOPIC,
                            finalEvent));
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        producer.close();
    }

    private static String getLatLong(String str) {
        str = str.replace("\t", "");
        str = str.replace("\n", "");

        String[] latLong = str.split("|");

        if (latLong.length == 2) {
            return latLong[1].trim() + "|" + latLong[0].trim();
        } else {
            return str;
        }
    }

    public static String[] GetKmlLanLangList(String urlString) throws ParserConfigurationException, SAXException,
            IOException {
        String[] array = null;
        String[] array2 = null;

        Document doc = null;
        String pathConent = "";
        File fXmlFile = new File(urlString);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(ClassLoader.getSystemResourceAsStream(urlString));
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("LineString");
        System.out.println(nList.getLength());
        int j = 0;
        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String strLatLon = eElement.getElementsByTagName("coordinates").item(0).getTextContent().toString();
                array = strLatLon.split(" ");
                array2 = new String[array.length];
                for (int i = 0; i < array.length; i++) {
                    array2[i] = array[i].replace(',', '|');
                }

            }

        }
        return array2;
    }
}
