package com.cs.core.config.interactor.model.mapping;

import java.util.List;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetAllMappingsResponseModel implements IGetAllMappingsResponseModel {
  
  private static final long                     serialVersionUID = 1L;
  protected List<IConfigEntityInformationModel> list;
  protected Long                                count;
  
  @Override
  public List<IConfigEntityInformationModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = MappingBasicInfoModel.class)
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
