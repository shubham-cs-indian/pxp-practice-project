package com.cs.core.config.interactor.model.template;

import com.cs.core.config.interactor.model.customtemplate.ICustomTemplateModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetGridTemplatesResponseModel extends IModel {
  
  public static final String LIST           = "list";
  public static final String COUNT          = "count";
  public static final String CONFIG_DETAILS = "configDetails";
  
  public List<ICustomTemplateModel> getList();
  
  public void setList(List<ICustomTemplateModel> list);
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public IConfigDetailsForGridTemplatesModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGridTemplatesModel configDetails);
}
