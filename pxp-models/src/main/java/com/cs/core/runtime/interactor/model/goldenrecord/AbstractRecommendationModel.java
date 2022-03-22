package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.constants.CommonConstants;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY,
    property = "propertyType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(name = CommonConstants.ATTRIBUTE_PROPERTY,
        value = AttributeRecommendationModel.class),
    @JsonSubTypes.Type(name = CommonConstants.RELATIONSHIP_PROPERTY,
        value = RelationshipRecommendationModel.class),
    @JsonSubTypes.Type(name = CommonConstants.TAG_PROPERTY, value = TagRecommendationModel.class) })
public abstract class AbstractRecommendationModel implements IRecommendationModel {
  
  private static final long serialVersionUID = 1L;
  protected String          propertyId;
  protected String          propertyType;
  protected String          contentId;
  protected String          supplierId;
  protected Long            lastModified;
  
  @Override
  public String getPropertyId()
  {
    return propertyId;
  }
  
  @Override
  public void setPropertyId(String propertyId)
  {
    this.propertyId = propertyId;
  }
  
  @Override
  public String getPropertyType()
  {
    return propertyType;
  }
  
  @Override
  public void setPropertyType(String propertyType)
  {
    this.propertyType = propertyType;
  }
  
  @Override
  public String getcontentId()
  {
    return contentId;
  }
  
  @Override
  public void setcontentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public String getSupplierId()
  {
    return supplierId;
  }
  
  @Override
  public void setSupplierId(String supplierId)
  {
    this.supplierId = supplierId;
  }
  
  @Override
  public Long getLastModified()
  {
    return lastModified;
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    this.lastModified = lastModified;
  }
}
