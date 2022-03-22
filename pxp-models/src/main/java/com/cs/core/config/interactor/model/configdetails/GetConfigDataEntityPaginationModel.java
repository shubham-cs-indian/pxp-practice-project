package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetConfigDataEntityPaginationModel implements IGetConfigDataEntityPaginationModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected Long                from;
  protected Long                size;
  protected String              sortBy;
  protected String              sortOrder;
  protected Map<String, Object> properties;
  protected List<String>        types;
  protected List<String>        typesToExclude;
  protected Boolean             isLanguageDependent;
  protected Boolean             isLanguageIndependent;
  protected Boolean             isDisabled;
  protected List<String>        idsToExclude;
  
  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
  
  @Override
  public Long getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Long size)
  {
    this.size = size;
  }
  
  @Override
  public String getSortBy()
  {
    return sortBy;
  }
  
  @Override
  public void setSortBy(String sortBy)
  {
    this.sortBy = sortBy;
  }
  
  @Override
  public String getSortOrder()
  {
    return sortOrder;
  }
  
  @Override
  public void setSortOrder(String sortOrder)
  {
    this.sortOrder = sortOrder;
  }
  
  @Override
  public Map<String, Object> getProperties()
  {
    return properties;
  }
  
  @Override
  public void setProperties(Map<String, Object> properties)
  {
    this.properties = properties;
  }
  
  @Override
  public List<String> getTypes()
  {
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
  @Override
  public List<String> getTypesToExclude()
  {
    return typesToExclude;
  }
  
  @Override
  public void setTypesToExclude(List<String> typesToExclude)
  {
    this.typesToExclude = typesToExclude;
  }
  
  @Override
  public Boolean getIsLanguageDependent()
  {
    if (isLanguageDependent == null) {
      isLanguageDependent = false;
    }
    return isLanguageDependent;
  }
  
  @Override
  public void setIsLanguageDependent(Boolean isLanguageDependent)
  {
    this.isLanguageDependent = isLanguageDependent;
  }
  
  @Override
  public Boolean getIsLanguageIndependent()
  {
    if (isLanguageIndependent == null) {
      isLanguageIndependent = false;
    }
    return isLanguageIndependent;
  }
  
  @Override
  public void setIsLanguageIndependent(Boolean isLanguageIndependent)
  {
    this.isLanguageIndependent = isLanguageIndependent;
  }
  
  public Boolean getIsDisabled()
  {
    return this.isDisabled;
  }
  
  @Override
  public void setIsDisabled(Boolean isDisabled)
  {
    this.isDisabled = isDisabled;
  }

  @Override
  public List<String> getIdsToExclude()
  {
    if (idsToExclude == null) {
      idsToExclude = new ArrayList<String>();
    }
    return idsToExclude;
  }

  @Override
  public void setIdsToExclude(List<String> idsToExclude)
  {
    this.idsToExclude = idsToExclude;
  }
}
