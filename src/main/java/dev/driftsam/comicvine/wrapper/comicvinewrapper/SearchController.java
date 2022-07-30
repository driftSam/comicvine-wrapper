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


    /**
     * @param queryString
     * @return
     * 
     * This works as expected now. Adding the User Agent header with any unique String was enough.
     * Removing the User Agent and printing the String (instead of the ComicVineResponse) will show you
     * the HTML/JS that talked about "No Wordpress Scrappers allowed".
     */
    @GetMapping(value="/series", produces = MediaType.APPLICATION_JSON_VALUE )
    public ComicVineResponse searchForSeries(@RequestParam(name = "query") String queryString){
        HttpHeaders headers = new HttpHeaders();

        // Removing this will get you the response that was gaslighting me. 
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

    /**
     * @param queryString
     * @throws InterruptedException
     * @throws ExecutionException
     * 
     * This is the one that is super strange. If you modify the method to return Mono<String> it'll redirect to the comicvine site.
     * But if you call the method as-is and print out the String response body it's a bunch of JS in an HTML script tag.
     * The HTML/JS is completely different than the HTML/JS that got returned when using the RestTemplate w/o user agent header
     * And adding/removing the user agent header here made no difference. 
     *
     */
    @GetMapping(value="/wc/series", produces = MediaType.APPLICATION_JSON_VALUE )
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

    /**
     * @return
     * Last strange thing, blocking in a GetMapping method works now, but only after I added 
     * spring-boot-starter-web to the pom. 
     */
    @GetMapping("/ye")
    public String yeQuote(){
        WebClient yeClient =  WebClient.create("https://api.kanye.rest/");
        return yeClient.get().accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(String.class).block();
    }

}
