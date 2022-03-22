package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IPropertyAggregationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;

public class PropertyAggregationDTO extends BaseAggregationDTO implements IPropertyAggregationDTO {

  private IPropertyDTO property;
  private String       bucketSearch;
  private Boolean      isTranslatable;

  public PropertyAggregationDTO(IPropertyDTO property, Boolean isTranslatable)
  {
    this.property = property;
    this.isTranslatable = isTranslatable;
    this.setAggregationType(AggregationType.byProperty);
  }

  @Override
  public String getBucketSearch()
  {
    return bucketSearch;
  }

  @Override
  public Boolean getIsTranslatable()
  {
    return isTranslatable;
  }

  @Override
  public void setBucketSearch(String bucketSearch)
  {
    this.bucketSearch = bucketSearch;
  }

  @Override
  public IPropertyDTO getProperty()
  {
    return property;
  }

  @Override
  public void setProperty(IPropertyDTO property)
  {
    this.property = property;
  }
}
