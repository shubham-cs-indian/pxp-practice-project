package com.cs.core.config.interactor.model.taxonomyhierarchy;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ITaxonomyHierarchyModel extends IModel {
  
  public static final String LIST                  = "list";
  public static final String CONFIG_DETAILS        = "configDetails";
  public static final String COUNT                  = "count";
  
  public List<IConfigTaxonomyHierarchyInformationModel> getList();
  public void setList(List<IConfigTaxonomyHierarchyInformationModel> list);
  
  public Map<String, IConfigTaxonomyHierarchyInformationModel> getConfigDetails();
  public void setConfigDetails(Map<String, IConfigTaxonomyHierarchyInformationModel> configDetails);
  
  public Long getCount();
  public void setCount(Long count);
}