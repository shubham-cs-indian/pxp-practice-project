package com.cs.core.config.interactor.model.configdetails;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigDataEntityPaginationModel extends IModel {
  
  public static final String FROM                    = "from";
  public static final String SIZE                    = "size";
  public static final String SORT_BY                 = "sortBy";
  public static final String SORT_ORDER              = "sortOrder";
  public static final String PROPERTIES              = "properties";
  public static final String TYPES                   = "types";
  public static final String TYPES_TO_EXCLUDE        = "typesToExclude";
  public static final String IS_LANGUAGE_DEPENDENT   = "isLanguageDependent";
  public static final String IS_LANGUAGE_INDEPENDENT = "isLanguageIndependent";
  public static final String IS_DISABLED             = "isDisabled";
  public static final String IDS_TO_EXCLUDE          = "idsToExclude";
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getSize();
  
  public void setSize(Long size);
  
  public String getSortBy();
  
  public void setSortBy(String sortBy);
  
  public String getSortOrder();
  
  public void setSortOrder(String sortOrder);
  
  public Map<String, Object> getProperties();
  
  public void setProperties(Map<String, Object> properties);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public List<String> getTypesToExclude();
  
  public void setTypesToExclude(List<String> typesToExclude);
  
  public Boolean getIsLanguageDependent();
  
  public void setIsLanguageDependent(Boolean isLanguageDependent);
  
  public Boolean getIsLanguageIndependent();
  
  public void setIsLanguageIndependent(Boolean isLanguageIndependent);
  
  public Boolean getIsDisabled();
  
  public void setIsDisabled(Boolean isDisabled);
  
  public List<String> getIdsToExclude();
  
  public void setIdsToExclude(List<String> idsToExclude);
}
