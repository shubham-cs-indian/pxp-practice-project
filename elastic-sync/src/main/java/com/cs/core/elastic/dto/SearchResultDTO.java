package com.cs.core.elastic.dto;

import com.cs.core.elastic.idto.ISearchResultDTO;

import java.util.*;

public class SearchResultDTO implements ISearchResultDTO {

  List<String>                     baseEntityIIDs;
  Long                             totalCount;
  Map<String, List<Map<String, Object>>> highlightsMap = new HashMap<>();

  public List<String> getBaseEntityIIDs()
  {
    return baseEntityIIDs;
  }

  public void setBaseEntityIIDs(List<String> baseEntityIIDs)
  {
    this.baseEntityIIDs = baseEntityIIDs;
  }

  public Long getTotalCount()
  {
    return totalCount;
  }

  public void setTotalCount(Long totalCount)
  {
    this.totalCount = totalCount;
  }

  @Override
  public Map<String, List<Map<String, Object>>> getHighlightsMap()
  {
    return this.highlightsMap;
  }

  @Override
  public void setHighlightsMap(Map<String, List<Map<String, Object>>> highlightsMap)
  {
    this.highlightsMap = highlightsMap;
  }
}
