package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetNatureKlassResponseModel implements IGetNatureKlassResponseModel {
  
  private static final long                     serialVersionUID = 1L;
  
  protected Long                                from;
  protected Long                                size;
  protected Long                                totalCount;
  protected List<IConfigEntityInformationModel> list;
  
  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
  
  @Override
  public Long getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Long size)
  {
    this.size = size;
  }
  
  @Override
  public Long getTotalCount()
  {
    return totalCount;
  }
  
  @Override
  public void setTotalCount(Long totalCount)
  {
    this.totalCount = totalCount;
  }
  
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
}
