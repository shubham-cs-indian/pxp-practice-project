package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetImmediateMajorTaxonomiesResponseModel
    implements IGetImmediateMajorTaxonomiesResponseModel {
  
  private static final long                     serialVersionUID = 1L;
  
  protected List<IConfigEntityInformationModel> list;
  protected Long                                count;
  
  @Override
  public List<IConfigEntityInformationModel> getList()
  {
    if (list == null) {
      list = new ArrayList<>();
    }
    return list;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setList(List<IConfigEntityInformationModel> taskList)
  {
    this.list = taskList;
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
}
