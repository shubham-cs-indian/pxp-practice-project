package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.config.interactor.model.user.UserBasicInfoModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetConfigDataUserResponseModel extends GetConfigDataEntityResponseModel
    implements IGetConfigDataUserResponseModel {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public List<IConfigEntityInformationModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = UserBasicInfoModel.class)
  public void setList(List<IConfigEntityInformationModel> list)
  {
    this.list = list;
  }
  
}
