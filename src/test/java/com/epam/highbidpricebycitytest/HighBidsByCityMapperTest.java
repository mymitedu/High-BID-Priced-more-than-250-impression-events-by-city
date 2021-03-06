package com.epam.highbidpricebycitytest;

import com.epam.highbidpricebycity.HighBidsByCityMapper;
import com.epam.highbidpricebycity.comparable.TextPair;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class HighBidsByCityMapperTest {
    private MapDriver<LongWritable, Text, TextPair, IntWritable> mapDriver;

    @Before
    public void setUp() {
        HighBidsByCityMapper mapper = new HighBidsByCityMapper();
        mapDriver = MapDriver.newMapDriver(mapper);
    }

    @Test
    public void testMapper() throws IOException {
        // replaced values from real entries with ordinal numbers for ease of testing;
        // kept user agent (index is 4), city id (7) and bidding price (19) normal
        String userAgentStr = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0)";
        mapDriver.withInput(new LongWritable(), new Text("0\t1\t2\t3\t" + userAgentStr + "\t5\t6\t101\t8\t9\t10\t11\t12\t13\t14\t15\t16\t17\t18\t251\t20\t21\t22\t23\n"));
        mapDriver.withInput(new LongWritable(), new Text("0\t1\t2\t3\t" + userAgentStr + "\t5\t6\t102\t8\t9\t10\t11\t12\t13\t14\t15\t16\t17\t18\t473\t20\t21\t22\t23\n"));
        mapDriver.withInput(new LongWritable(), new Text("0\t1\t2\t3\t" + userAgentStr + "\t5\t6\t103\t8\t9\t10\t11\t12\t13\t14\t15\t16\t17\t18\t251\t20\t21\t22\t23\n"));
        String os = new UserAgent(userAgentStr).getOperatingSystem().getName();
        IntWritable one = new IntWritable(1);
        mapDriver.withOutput(new TextPair("Hyderabad", os), one);
        mapDriver.withOutput(new TextPair("Bangalore", os), one);
        mapDriver.withOutput(new TextPair("Pune", os), one);
        mapDriver.withCacheFile("city.txt").runTest();
    }

    @Test
    public void testUnknownCity() throws IOException {
        String userAgentStr = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0)";
        mapDriver.withInput(new LongWritable(), new Text("0\t1\t2\t3\t" + userAgentStr + "\t5\t6\t101\t8\t9\t10\t11\t12\t13\t14\t15\t16\t17\t18\t251\t20\t21\t22\t23\n"));
        mapDriver.withInput(new LongWritable(), new Text("0\t1\t2\t3\t" + userAgentStr + "\t5\t6\t104\t8\t9\t10\t11\t12\t13\t14\t15\t16\t17\t18\t358\t20\t21\t22\t23\n"));
        String os = new UserAgent(userAgentStr).getOperatingSystem().getName();
        IntWritable one = new IntWritable(1);
        mapDriver.withOutput(new TextPair("Hyderabad", os), one);
        mapDriver.withOutput(new TextPair("unknown", os), one);
        mapDriver.withCacheFile("city.txt").runTest();
    }

    @Test
    public void testLesserBIDPrice() throws IOException {
        String userAgentStr = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0)";
        mapDriver.withInput(new LongWritable(), new Text("0\t1\t2\t3\t" + userAgentStr + "\t5\t6\t101\t8\t9\t10\t11\t12\t13\t14\t15\t16\t17\t18\t251\t20\t21\t22\t23\n"));
        mapDriver.withInput(new LongWritable(), new Text("0\t1\t2\t3\t" + userAgentStr + "\t5\t6\t103\t8\t9\t10\t11\t12\t13\t14\t15\t16\t17\t18\t250\t20\t21\t22\t23\n"));
        String os = new UserAgent(userAgentStr).getOperatingSystem().getName();
        IntWritable one = new IntWritable(1);
        mapDriver.withOutput(new TextPair("Hyderabad", os), one);
        mapDriver.withCacheFile("city.txt").runTest();
    }
}
