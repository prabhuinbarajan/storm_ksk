package com.hippocamp.ksk.storm;


import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import java.util.Map;
import org.apache.log4j.Logger;

import backtype.storm.tuple.Fields;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;


public class LogTruckEventsBolt extends BaseRichBolt
{
    private static final Logger LOG = Logger.getLogger(LogTruckEventsBolt.class);
    
    OutputCollector collector;
    public static final Fields boltFields = new Fields(TruckScheme.FIELD_DRIVER_ID, TruckScheme.FIELD_TRUCK_ID,TruckScheme.FIELD_EVENT_TIME, TruckScheme.FIELD_EVENT_TYPE, TruckScheme.FIELD_LATITUDE, TruckScheme.FIELD_LONGITUDE);
    public void declareOutputFields(OutputFieldsDeclarer ofd) 
    {
	ofd.declareStream("logEvents", boltFields);	
    }

    public void prepare(Map map, TopologyContext tc, OutputCollector oc) 
    {
       //no output.
	collector = oc;
    }


    public void execute(Tuple tuple) 
    {
      LOG.info(tuple.getStringByField(TruckScheme.FIELD_DRIVER_ID)  + "," + 
              tuple.getStringByField(TruckScheme.FIELD_TRUCK_ID)    + "," +
              tuple.getValueByField(TruckScheme.FIELD_EVENT_TIME)  + "," +
              tuple.getStringByField(TruckScheme.FIELD_EVENT_TYPE)  + "," +
              tuple.getStringByField(TruckScheme.FIELD_LATITUDE)    + "," +
              tuple.getStringByField(TruckScheme.FIELD_LONGITUDE));
      	      collector.emit ("logEvents", tuple, new Values(tuple.getStringByField(TruckScheme.FIELD_DRIVER_ID), tuple.getStringByField(TruckScheme.FIELD_TRUCK_ID), tuple.getValueByField(TruckScheme.FIELD_EVENT_TIME) , tuple.getStringByField(TruckScheme.FIELD_EVENT_TYPE), tuple.getStringByField(TruckScheme.FIELD_LATITUDE), tuple.getStringByField(TruckScheme.FIELD_LONGITUDE)));
    }
    
}
