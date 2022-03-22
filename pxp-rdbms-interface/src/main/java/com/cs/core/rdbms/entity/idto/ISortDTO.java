package com.cs.core.rdbms.entity.idto;

public interface ISortDTO {

  enum SortOrder {
    asc, desc
  }

  public String getSortField();
  
  public SortOrder getSortOrder();
  
  public Boolean getIsNumeric();

  public Boolean getIsLanguageDependent();
  public void setIsLanguageDependent(Boolean languageDependent);
}
