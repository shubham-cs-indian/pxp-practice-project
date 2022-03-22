package com.cs.di.config.model.authorization;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetAllPartnerAuthorizationModel
    implements IGetAllPartnerAuthorizationModel {
  
  private static final long                     serialVersionUID = 1L;
  protected List<IConfigEntityInformationModel> list;
  protected Long                                count;
  
  @Override
  public List<IConfigEntityInformationModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setList(List<IConfigEntityInformationModel> list)
  {
    this.list = list;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
}
