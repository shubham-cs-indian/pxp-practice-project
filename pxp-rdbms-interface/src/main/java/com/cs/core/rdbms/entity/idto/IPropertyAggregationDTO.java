package com.cs.core.rdbms.entity.idto;

import com.cs.core.rdbms.config.idto.IPropertyDTO;

public interface IPropertyAggregationDTO extends IAggregationRequestDTO{

  /**
   *
   * @param property property on which aggregation will be applied.
   */
  public void setProperty(IPropertyDTO property);

  /**
   *
   * @return return property that needs to be filtered
   */
  public IPropertyDTO getProperty();

  /**
   * @param bucketSearch search string for buckets.
   */
  public void setBucketSearch(String bucketSearch);

  /**
   * @return string to be searched in buckets
   */
  public String getBucketSearch();

  Boolean getIsTranslatable();
}
