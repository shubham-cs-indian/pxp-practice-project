package com.cs.core.config.interactor.model.permission;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IPermissionWithHierarchyRequestModel extends IModel {
  
  public static final String ID          = "id";
  public static final String ROLE_ID     = "roleId";
  public static final String ENTITY_TYPE = "entityType";
  public static final String FROM        = "from";
  public static final String SIZE        = "size";
  public static final String SEARCH_TEXT = "searchText";
  public static final String TAXONOMY_TYPE = "taxonomyType";
  
  public String getId();
  
  public void setId(String id);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getSize();
  
  public void setSize(Long size);
  
  public String getSearchText();
  
  public void setSearchText(String searchText);
  
  public String getTaxonomyType();
  
  public void setTaxonomyType(String taxonomyType);
}
