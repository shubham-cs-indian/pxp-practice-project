package com.cs.core.config.interactor.model.grid;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IConfigGetAllRequestModel extends IModel {
  
  public static final String FROM             = "from";
  public static final String SIZE             = "size";
  public static final String SORT_BY          = "sortBy";
  public static final String SEARCH_TEXT      = "searchText";
  public static final String SORT_ORDER       = "sortOrder";
  public static final String SEARCH_COLUMN    = "searchColumn";
  public static final String TYPES            = "types";
  public static final String IS_STANDARD      = "isStandard";
  public static final String BASETYPE         = "baseType";
  public static final String DASHBOARD_TAB_ID = "dashboardTabId";
  public static final String USER_ID          = "userId";
  public static final String IS_ABSTRACT      = "isAbstract";
  public static final String IDS_TO_EXCLUDE   = "idsToExclude";
  public static final String WORKFLOW_TYPE    = "workflowType";
  public static final String ENTITY_TYPE      = "entityType";
  public static final String IDS              = "ids";
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getSize();
  
  public void setSize(Long size);
  
  public String getSortBy();
  
  public void setSortBy(String sortBy);
  
  public String getSearchText();
  
  public void setSearchText(String searchText);
  
  public String getSortOrder();
  
  public void setSortOrder(String sortOrder);
  
  public String getSearchColumn();
  
  public void setSearchColumn(String searchColumn);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
  
  public String getDashboardTabId();
  
  public void setDashboardTabId(String dashboardTabId);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Boolean getIsAbstract();
  
  public void setisAbstract(Boolean isAbstract);
  
  public List<String> getIdsToExclude();
  
  public void setIdsToExclude(List<String> idsToExclude);
  
  public String getWorkflowType();
  public void setWorkflowType(String workflowType);
  
  public String getEntityType();
  public void setEntityType(String entityType);
  
  public List<String> getIds();
  public void setIds(List<String> ids);
  
}
