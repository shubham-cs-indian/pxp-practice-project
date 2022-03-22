package com.cs.core.elastic.das;

import com.cs.core.elastic.idto.ISearchResultDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO;
import com.cs.core.rdbms.entity.idto.IAggregationResultDTO;
import com.cs.core.rdbms.entity.idto.IMultiplePropertyAggregationDTO;
import com.cs.core.rdbms.entity.idto.IMultiplePropertyAggregationResultDTO;

import java.io.IOException;
import java.util.List;

public interface ISearchDAO {

  ISearchResultDTO search() throws Exception;

  ISearchResultDTO tableView() throws Exception;

  IAggregationResultDTO aggregation(IAggregationRequestDTO request) throws IOException;

  public IMultiplePropertyAggregationResultDTO multiplePropertyAggregation(IMultiplePropertyAggregationDTO request) throws IOException;

  List<String> findFilterable(List<IPropertyDTO> properties) throws IOException;

  public enum Aggregation {
    byClass, byTaxonomy, byAttribute, byTag;
  }
  
  /**
   * 
   * @return count of Entity
   * @throws IOException
   */
  public Long getCountForEntityUsage() throws IOException;

}
