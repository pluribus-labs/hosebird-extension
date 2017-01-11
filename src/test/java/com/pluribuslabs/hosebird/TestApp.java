package com.pluribuslabs.hosebird;

import com.pluribuslabs.hosebird.v1.IndustrySentimentStreamingEndpoint;
import com.pluribuslabs.hosebird.v1.SectorSentimentStreamingEndpoint;
import com.pluribuslabs.hosebird.v1.SymbolSentimentStreamingEndpoint;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.endpoint.StreamingEndpoint;
import com.twitter.hbc.core.processor.LineStringProcessor;
import com.twitter.hbc.httpclient.auth.BasicAuth;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by sebastien on 1/10/17.
 */
public class TestApp {
    private static final String USER_NAME = "dummy_username";
    private static final String PASSWORD = "dummy_password";
    private static final StreamingEndpoint SYMBOL_ENDPOINT = new SymbolSentimentStreamingEndpoint("MSFT");
    private static final StreamingEndpoint SECTOR_ENDPOINT = new SectorSentimentStreamingEndpoint("ConsumerDurables");
    private static final StreamingEndpoint INDUSTRY_ENDPOINT = new IndustrySentimentStreamingEndpoint("CommercialServices", "MiscellaneousCommercialServices");

    public static void main(String... args) throws Exception {

        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10000);

        Client client = new ClientBuilder()
                .name("PlabsClientTest-01")
                .hosts(Constants.SENTIMENT_STREAM_HOST_V1)
                .endpoint(SYMBOL_ENDPOINT)
                .authentication(new BasicAuth(USER_NAME, PASSWORD))
                .connectionTimeout(60_000)
                .processor(new LineStringProcessor(queue))
                .build();
        client.connect();

        Thread t = new Thread(() -> {
            while(!client.isDone()) {
                while (!queue.isEmpty())
                    System.out.println(queue.poll());
            }
        });

        t.start();

        Thread.sleep(Long.MAX_VALUE);
    }
}
