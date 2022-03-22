package com.cs.core.config.interactor.model.template;

import com.cs.core.config.interactor.model.customtemplate.CustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.ICustomTemplateModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetGridTemplatesResponseModel implements IGetGridTemplatesResponseModel {
  
  private static final long                     serialVersionUID = 1L;
  protected List<ICustomTemplateModel>          list;
  protected Long                                count;
  protected IConfigDetailsForGridTemplatesModel configDetails;
  
  @Override
  public List<ICustomTemplateModel> getList()
  {
    if (list == null) {
      list = new ArrayList<>();
    }
    return list;
  }
  
  @JsonTypeInfo(use = Id.NONE)
  @JsonDeserialize(contentAs = CustomTemplateModel.class)
  @Override
  public void setList(List<ICustomTemplateModel> list)
  {
    this.list = list;
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
  
  @Override
  public IConfigDetailsForGridTemplatesModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForGridTemplatesModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForGridTemplatesModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
