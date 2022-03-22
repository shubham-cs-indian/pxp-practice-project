package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.config.interactor.model.attribute.AttributeBasicInfoModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetConfigDataAttributeResponseModel extends GetConfigDataEntityResponseModel
    implements IGetConfigDataAttributeResponseModel {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public List<IConfigEntityInformationModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = AttributeBasicInfoModel.class)
  public void setList(List<IConfigEntityInformationModel> list)
  {
    this.list = list;
  }
}
