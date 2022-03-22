package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetFilterAndSortDataRequestModel extends IModel {
  
  public static final String TYPE_IDS      = "typeIds";
  public static final String ATTRIBUTE_IDS = "attributeIds";
  public static final String TAG_IDS       = "tagIds";
  public static final String ROLE_IDS      = "roleIds";
  public static final String MODULE_ID     = "moduleId";
  
  public List<String> getTypeIds();
  
  public void setTypeIds(List<String> typeIds);
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public List<String> getTagIds();
  
  public void setTagIds(List<String> tagIds);
  
  public List<String> getRoleIds();
  
  public void setRoleIds(List<String> roleIds);
  
  public String getModuleId();
  
  public void setModuleId(String moduleId);
}
