package com.cs.core.runtime.interactor.model.typeswitch;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IAllowedTypesRequestModel extends IModel {
  
  public static final String BASE_TYPE      = "baseType";
  public static final String SELECTION_TYPE = "selectionType";
  public static final String ID             = "id";
  public static final String USER_ID        = "userId";
  public static final String FROM           = "from";
  public static final String SIZE           = "size";
  public static final String SEARCH_TEXT    = "searchText";
  public static final String SEARCH_COLUMN  = "searchColumn";
  public static final String IDS_TO_EXCLUDE = "idsToExclude";
  public static final String MODULE_ID      = "moduleId";
  public static final String ALLOWED_ENTITIES = "allowedEntities";
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getSelectionType();
  
  public void setSelectionType(String selectionType);
  
  public String getId();
  
  public void setId(String id);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getSize();
  
  public void setSize(Long size);
  
  public String getSearchText();
  
  public void setSearchText(String searchText);
  
  public String getSearchColumn();
  
  public void setSearchColumn(String searchColumn);
  
  public List<String> getIdsToExclude();
  
  public void setIdsToExclude(List<String> idsToExclude);
  
  public String getModuleId();
  public void setModuleId(String moduleId);
  
  public List<String> getAllowedEntities();
  public void setAllowedEntities(List<String> allowedEntities);
}
