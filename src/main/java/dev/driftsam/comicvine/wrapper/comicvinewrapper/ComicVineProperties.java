package dev.driftsam.comicvine.wrapper.comicvinewrapper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "comic-vine")
public class ComicVineProperties {
    
    private String baseUrl;
    private String apiKey;
    private String format;

    public String getBaseUrl() {
        return baseUrl;
    }
    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }
    public String getApiKey() {
        return apiKey;
    }
    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(final String format) {
        this.format = format;
    }
    
}
