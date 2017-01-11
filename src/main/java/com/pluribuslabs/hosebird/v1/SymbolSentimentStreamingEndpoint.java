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

@Data
public class SymbolSentimentStreamingEndpoint implements StreamingEndpoint {
    private static final String BASE_PATH = "/stream/sentiment.json/v1/symbol/%s";
    protected final ConcurrentMap<String, String> queryParameters = Maps.newConcurrentMap();
    protected final String symbol;

    /**
     * Requests the sentiment data for all symbols.
     */
    public SymbolSentimentStreamingEndpoint() {
        this("*");
    }

    /**
     * Requests the sentiment data for the given symbol.
     * @param symbol
     */
    public SymbolSentimentStreamingEndpoint(String symbol){
        Preconditions.checkArgument(!Strings.isNullOrEmpty(symbol), "symbol cannot be null or empty");
        this.symbol = symbol;
    }


    @Override
    public String getURI() {
        String uri = String.format(BASE_PATH, symbol.trim());

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
