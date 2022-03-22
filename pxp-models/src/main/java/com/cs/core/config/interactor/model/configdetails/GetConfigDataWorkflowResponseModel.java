package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.config.interactor.model.processevent.ProcessEventBasicInfoModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetConfigDataWorkflowResponseModel extends GetConfigDataEntityResponseModel implements IGetConfigDataWorkflowResponseModel {
 
  private static final long serialVersionUID = 1L;
  
  @Override
  public List<IConfigEntityInformationModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = ProcessEventBasicInfoModel.class)
  public void setList(List<IConfigEntityInformationModel> list)
  {
    this.list = list;
  }
  
}
