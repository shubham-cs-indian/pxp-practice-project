package com.cs.core.config.interactor.model.attribute.standard;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.attribute.IBulkSaveAttributeSuccessModel;
import com.cs.core.config.interactor.model.attribute.IConfigDetailsForGridAttributesModel;
import com.cs.core.config.interactor.model.attribute.IUpdatedAttributeListModel;
import com.cs.core.config.interactor.model.attribute.UpdatedAttributeListModel;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveAttributeSuccessModel implements IBulkSaveAttributeSuccessModel {
  
  private static final long                      serialVersionUID = 1L;
  
  protected List<IAttributeModel>                attributesList       = new ArrayList<>();
  protected IConfigDetailsForGridAttributesModel configDetails;
  protected List<IUpdatedAttributeListModel>     updatedAttributeList = new ArrayList<>();
  
  @Override
  public List<IAttributeModel> getAttributesList()
  {
    return attributesList;
  }
  
  @JsonTypeInfo(use = Id.NONE)
  @JsonDeserialize(contentAs = AbstractAttributeModel.class)
  @Override
  public void setAttributesList(List<IAttributeModel> attributesList)
  {
    this.attributesList = attributesList;
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
  
  @Override
  public List<IUpdatedAttributeListModel> getUpdatedAttributeList()
  {
    return updatedAttributeList;
  }
  
  @JsonDeserialize(contentAs = UpdatedAttributeListModel.class)
  @Override
  public void setUpdatedAttributeList(List<IUpdatedAttributeListModel> updatedAttributeList)
  {
    this.updatedAttributeList = updatedAttributeList;
  }
}
