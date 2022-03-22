package com.cs.core.rdbms.entity.idto;

import java.util.Collection;

/**
 * @author faizan.siddique
 * This interface is used for Range Query's aggregation.
 */
public interface IRangeAggregationDTO extends IPropertyAggregationDTO{
  
  /**
   * Returns the base query required for getting all range values.
   * @return Base Query.
   */
  Object getBaseQuery();
  
  /**
   * Sets the base Query required for getting all range values.
   * @param baseQuery
   */
  void setBaseQuery(Object baseQuery);
  
  /**
   * Sets Indices for range values aggregation.
   * @param indices
   */
  void setIndices(Collection<String> indices);
  
  /**
   * Returns Indices for range values aggregation.
   * @return Indices.
   */
  Collection<String> getIndices();
  
}
