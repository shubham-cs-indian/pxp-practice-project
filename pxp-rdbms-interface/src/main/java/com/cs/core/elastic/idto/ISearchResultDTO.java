package com.cs.core.elastic.idto;

import java.util.List;
import java.util.Map;

public interface ISearchResultDTO {

  public List<String> getBaseEntityIIDs();

  public void setBaseEntityIIDs(List<String> baseEntityIIDs);

  public Long getTotalCount();

  public void setTotalCount(Long totalCount);
  
  Map<String, List<Map<String, Object>>> getHighlightsMap();
  void setHighlightsMap(Map<String, List<Map<String, Object>>> highlightsMap);

}
