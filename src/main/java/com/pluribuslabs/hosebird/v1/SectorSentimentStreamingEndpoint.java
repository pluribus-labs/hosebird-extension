package com.pluribuslabs.hosebird.v1;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.twitter.hbc.core.HttpConstants;
import com.twitter.hbc.core.endpoint.StreamingEndpoint;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by sebastien on 1/10/17.
 */
@Data
public class SectorSentimentStreamingEndpoint implements StreamingEndpoint {
    private static final String BASE_PATH = "/stream/sentiment.json/v1/sector/%s";
    protected final ConcurrentMap<String, String> queryParameters = Maps.newConcurrentMap();
    protected final String sectorCode;

    /**
     * Requests the sentiment data for all sectors.
     */
    public SectorSentimentStreamingEndpoint() {
        this("*");
    }

    /**
     * Requests the sentiment data for the given sector.
     * @param sectorCode the sector code
     */
    public SectorSentimentStreamingEndpoint(String sectorCode){
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sectorCode), "sectorCode cannot be null or empty");
        this.sectorCode = sectorCode;
    }


    @Override
    public String getURI() {
        String uri = String.format(BASE_PATH, sectorCode.trim());

        if (queryParameters.isEmpty()) {
            return uri;
        } else {
            return uri + "?" + generateParamString(queryParameters);
        }
    }

    protected String generateParamString(Map<String, String> params) {
        return Joiner.on("&")
                .withKeyValueSeparator("=")
                .join(params);
    }

    @Override
    public String getHttpMethod() {
        return HttpConstants.HTTP_GET;
    }

    @Override
    public String getPostParamString() {
        return null;
    }

    @Override
    public String getQueryParamString() {
        return generateParamString(queryParameters);
    }

    @Override
    public void addQueryParameter(String param, String value) {
        queryParameters.put(param, value);
    }

    @Override
    public void removeQueryParameter(String param) {
        queryParameters.remove(param);
    }

    // These don't do anything
    @Override
    public void setBackfillCount(int count) { }

    @Override
    public void setApiVersion(String apiVersion) { }

    @Override
    public void addPostParameter(String param, String value) { }

    @Override
    public void removePostParameter(String param) { }


}
