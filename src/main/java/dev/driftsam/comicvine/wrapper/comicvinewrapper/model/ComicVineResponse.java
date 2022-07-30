package dev.driftsam.comicvine.wrapper.comicvinewrapper.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ComicVineResponse (
    String error,
    Integer limit,
    Integer offset,

    @JsonProperty("number_of_page_results")
    Integer numberOfPageResults,

    @JsonProperty("number_of_total_results")
    Integer numberOfTotalResults,

    @JsonProperty("status_code")
    Integer statusCode,

    List<Result> results,
    String version
    ){}