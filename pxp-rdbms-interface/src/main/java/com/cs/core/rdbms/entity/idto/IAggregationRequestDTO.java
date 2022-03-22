package com.cs.core.rdbms.entity.idto;

public interface IAggregationRequestDTO {

  public enum AggregationType {
    byClass (IClassifierAggregationDTO.class), byTaxonomy(IClassifierAggregationDTO.class),
    byProperty(IPropertyAggregationDTO.class), byFlatField(IFlatFieldAggregationDTO.class),
    byRuleViolation(IRuleViolationAggregationDTO.class), byRange(IRangeAggregationDTO.class);

    Class<? extends IAggregationRequestDTO> requestType;
    AggregationType(Class<? extends IAggregationRequestDTO> requestType)
    {
      this.requestType = requestType;
    }
  }

  /**
   * @return return the type of aggregation performed.
   */
  public AggregationType getAggregationType();

  /**
   * @param aggregationType aggregation type that needs to set
   */
  public void setAggregationType(AggregationType aggregationType);

  /**
   * @return number of aggregation buckets
   */
  public int getSize();

  /**
   * @param size of aggregation buckets.
   */
  public void setSize(int size);

}
