package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.model.configdetails.GetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataTagResponseModel;
import com.cs.core.config.interactor.model.tag.TagBasicInfoModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetConfigDataTagResponseModel extends GetConfigDataEntityResponseModel
    implements IGetConfigDataTagResponseModel {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public List<IConfigEntityInformationModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagBasicInfoModel.class)
  public void setList(List<IConfigEntityInformationModel> list)
  {
    this.list = list;
  }
}
