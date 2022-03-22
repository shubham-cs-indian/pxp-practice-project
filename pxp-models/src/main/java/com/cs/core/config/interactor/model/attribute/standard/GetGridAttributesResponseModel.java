package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.attribute.IConfigDetailsForGridAttributesModel;
import com.cs.core.config.interactor.model.attribute.IGetGridAttributesResponseModel;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetGridAttributesResponseModel implements IGetGridAttributesResponseModel {
  
  private static final long                      serialVersionUID = 1L;
  
  protected List<IAttributeModel>                attributeList;
  protected Long                                 count;
  protected IConfigDetailsForGridAttributesModel configDetails;
  
  @Override
  public List<IAttributeModel> getAttributeList()
  {
    if (attributeList == null) {
      attributeList = new ArrayList<>();
    }
    return attributeList;
  }
  
  @JsonTypeInfo(use = Id.NONE)
  @JsonDeserialize(contentAs = AbstractAttributeModel.class)
  @Override
  public void setAttributeList(List<IAttributeModel> attributeList)
  {
    this.attributeList = attributeList;
  }
  
  @Override
  public Long getCount()
  {
    if (count == null) {
      count = new Long(0);
    }
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  @Override
  public IConfigDetailsForGridAttributesModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForGridAttributesModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForGridAttributesModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
