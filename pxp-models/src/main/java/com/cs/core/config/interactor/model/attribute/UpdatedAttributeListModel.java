package com.cs.core.config.interactor.model.attribute;

import java.util.List;

import com.cs.core.config.interactor.entity.attribute.AbstractConcatenatedOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class UpdatedAttributeListModel implements IUpdatedAttributeListModel {
  
  protected List<IConcatenatedOperator> attributeConcatenatedList;
  protected String                      propertyIID;
  protected String                      attributeId;
  
  @Override
  public List<IConcatenatedOperator> getAttributeConcatenatedList()
  {
    return attributeConcatenatedList;
  }
  
  @JsonDeserialize(contentAs = AbstractConcatenatedOperator.class)
  @Override
  public void setAttributeConcatenatedList(List<IConcatenatedOperator> attributeConcatenatedList)
  {
    this.attributeConcatenatedList = attributeConcatenatedList;
  }
  
  @Override
  public String getPropertyIID()
  {
    return propertyIID;
  }
  
  @Override
  public void setPropertyIID(String propertyIID)
  {
    this.propertyIID = propertyIID;
  }
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
  
}
