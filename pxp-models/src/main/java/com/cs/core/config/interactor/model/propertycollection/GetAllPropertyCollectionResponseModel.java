package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.runtime.interactor.model.propertycollection.IPropertyCollectionEntityInformationModel;
import com.cs.core.runtime.interactor.model.propertycollection.PropertyCollectionEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetAllPropertyCollectionResponseModel
    implements IGetAllPropertyCollectionResponseModel {
  
  private static final long                                 serialVersionUID = 1L;
  protected List<IPropertyCollectionEntityInformationModel> list;
  protected Long                                            count;
  
  @Override
  public List<IPropertyCollectionEntityInformationModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyCollectionEntityInformationModel.class)
  public void setList(List<IPropertyCollectionEntityInformationModel> list)
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
