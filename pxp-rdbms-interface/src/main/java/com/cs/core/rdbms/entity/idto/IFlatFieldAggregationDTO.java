package com.cs.core.rdbms.entity.idto;

public interface IFlatFieldAggregationDTO extends IAggregationRequestDTO{


  /**
   *
   * @return  flatField for aggregation
   */
  public String getFlatField();

  /**
   *
   * @param flatField flatField for aggregation
   */
  public void setFlatField(String flatField);

}
