package dev.driftsam.comicvine.wrapper.comicvinewrapper.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME, 
  include = JsonTypeInfo.As.EXISTING_PROPERTY, 
  property = "resource_type")
@JsonSubTypes({ 
    @Type(value = SeriesSearchResult.class, name = "volume")
  })
public interface Result {}
