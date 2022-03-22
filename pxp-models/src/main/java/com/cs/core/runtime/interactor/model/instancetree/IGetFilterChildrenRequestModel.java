package com.cs.core.runtime.interactor.model.instancetree;

public interface IGetFilterChildrenRequestModel extends INewInstanceTreeRequestModel {
  
  public static final String MODULE_ID           = "moduleId";
  public static final String ID                  = "id";
  public static final String FILTER_TYPE         = "filterType";
  public static final String REFERENCED_PROPERTY = "referencedProperty";
  public static final String IS_ARCHIVE_PORTAL   = "isArchivePortal";
  public static final String SEARCH_TEXT         = "searchText";
  
  public String getModuleId();
  public void setModuleId(String moduleId);
  
  public String getId();
  public void setId(String id);
  
  public String getFilterType();
  public void setFilterType(String filterType);
  
  public Boolean getIsArchivePortal();
  public void setIsArchivePortal(Boolean isArchivePortal);
  
  public String getSearchText();
  public void setSearchText(String searchText);
  
  public  IReferencedPropertyModel getReferencedProperty();
  public void setReferencedProperty(IReferencedPropertyModel referencedProperty);
}
