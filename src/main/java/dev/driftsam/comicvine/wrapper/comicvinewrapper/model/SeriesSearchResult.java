package dev.driftsam.comicvine.wrapper.comicvinewrapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SeriesSearchResult(

    @JsonProperty("count_of_issues")
    Integer countOfIssues,
    Integer id,
    String name

) implements Result{}
