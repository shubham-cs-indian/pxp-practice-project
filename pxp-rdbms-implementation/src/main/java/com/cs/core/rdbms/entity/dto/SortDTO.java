package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.entity.idto.ISortDTO;

public class SortDTO implements ISortDTO {

  protected String    sortField;
  protected SortOrder sortOrder;
  protected Boolean   isNumeric;
  protected Boolean   isLanguageDependent;

  public SortDTO(String sortField, SortOrder sortOrder, Boolean isNumeric)
  {
    super();
    this.sortField = sortField;
    this.sortOrder = sortOrder;
    this.isNumeric = isNumeric;
  }
  
  @Override
  public String getSortField()
  {
    return sortField;
  }
  
  @Override
  public SortOrder getSortOrder()
  {
    return sortOrder;
  }

  @Override
  public Boolean getIsNumeric()
  {
    return isNumeric;
  }

  @Override
  public Boolean getIsLanguageDependent()
  {
    return isLanguageDependent;
  }

  @Override
  public void setIsLanguageDependent(Boolean languageDependent)
  {
    isLanguageDependent = languageDependent;
  }

}
