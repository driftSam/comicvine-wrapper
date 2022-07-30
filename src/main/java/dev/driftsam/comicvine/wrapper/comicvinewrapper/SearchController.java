package dev.driftsam.comicvine.wrapper.comicvinewrapper;


import java.net.URI;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import dev.driftsam.comicvine.wrapper.comicvinewrapper.model.ComicVineResponse;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final ComicVineProperties properties;
    private final WebClient comicVineWebClient;
    private final RestTemplate restTemplate;

    public SearchController(ComicVineProperties properties, RestTemplateBuilder builder) {
        this.properties = properties;
        this.restTemplate = builder.build();
        this.comicVineWebClient = WebClient.create(properties.getBaseUrl());
    }


    @GetMapping(value="/series", produces = MediaType.APPLICATION_JSON_VALUE )
    public ComicVineResponse searchForSeries(@RequestParam(name = "query") String queryString){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "driftSam.dev");
       
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.getBaseUrl())
            .path("/search")
            .queryParam("api_key", properties.getApiKey())
            .queryParam("format", properties.getFormat())
            .queryParam("resources", "volume")
            .queryParam("query", queryString)
            .queryParam("field_list","name,start_year,publisher,id,image,count_of_issues")
            .build().toUri();
       
        var request = RequestEntity.get(uri).headers(headers).build();
       
        System.out.println(uri.toString());

        return restTemplate.exchange(request, ComicVineResponse.class).getBody();
    }

    @GetMapping(value="/wc/search/series", produces = MediaType.APPLICATION_JSON_VALUE )
    public void searchForSeriesWebClient(@RequestParam(name = "query") String queryString) throws InterruptedException, ExecutionException {
        Objects.requireNonNull(queryString);

        this.comicVineWebClient
            .get()
            .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .query("api_key={apiKey}&format={format}&resources=volume&query={queryString}")
                        .build(properties.getApiKey(), properties.getFormat(), queryString))
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.USER_AGENT, "driftSam.dev")
            .retrieve()
            .bodyToMono(String.class).subscribe(s->{
                System.out.println(s);
            });
            ;

    }

    @GetMapping("/ye")
    public String yeQuote(){
        WebClient yeClient =  WebClient.create("https://api.kanye.rest/");
        return yeClient.get().accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(String.class).block();
    }

}
