package com.cs.core.config.interactor.model.role;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAllowedTargetsForRoleRequestModel extends IModel {
  
  public static final String SELECTION_TYPE  = "selectionType";
  public static final String ID              = "id";
  public static final String FROM            = "from";
  public static final String SIZE            = "size";
  public static final String SEARCH_TEXT     = "searchText";
  public static final String ORGANIZATION_ID = "organizationId";
  public static final String SEARCH_COLUMN   = "searchColumn";
  
  public String getSelectionType();
  
  public void setSelectionType(String selectionType);
  
  public String getId();
  
  public void setId(String Id);
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getSize();
  
  public void setSize(Long size);
  
  public String getSearchText();
  
  public void setSearchText(String searchText);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getSearchColumn();
  
  public void setSearchColumn(String searchColumn);
}
