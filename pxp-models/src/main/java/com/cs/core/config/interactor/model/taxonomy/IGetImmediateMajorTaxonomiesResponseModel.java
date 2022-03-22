package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetImmediateMajorTaxonomiesResponseModel extends IModel {
  
  public static final String LIST  = "list";
  public static final String COUNT = "count";
  
  public List<IConfigEntityInformationModel> getList();
  
  public void setList(List<IConfigEntityInformationModel> list);
  
  public Long getCount();
  
  public void setCount(Long count);
}
