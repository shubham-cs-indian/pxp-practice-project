package com.cs.core.config.interactor.model.translations;

import java.util.List;

public class GetTranslationsRequestModel implements IGetTranslationsRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          entityType;
  protected List<String>    languages;
  protected Integer         from;
  protected Integer         size;
  protected String          searchLanguage;
  protected String          searchText;
  protected String          searchField;
  protected String          sortLanguage;
  protected String          sortOrder;
  protected String          sortBy;
  
  @Override
  public String getEntityType()
  {
    return entityType;
  }
  
  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
  
  @Override
  public List<String> getLanguages()
  {
    return languages;
  }
  
  @Override
  public void setLanguages(List<String> languages)
  {
    this.languages = languages;
  }
  
  @Override
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }
  
  @Override
  public String getSearchLanguage()
  {
    return searchLanguage;
  }
  
  @Override
  public void setSearchLanguage(String searchLanguage)
  {
    this.searchLanguage = searchLanguage;
  }
  
  @Override
  public String getSearchText()
  {
    return searchText;
  }
  
  @Override
  public void setSearchText(String searchText)
  {
    this.searchText = searchText;
  }
  
  @Override
  public String getSearchField()
  {
    return searchField;
  }
  
  @Override
  public void setSearchField(String searchField)
  {
    this.searchField = searchField;
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
  public String getSortLanguage()
  {
    return sortLanguage;
  }
  
  @Override
  public void setSortLanguage(String sortLanguage)
  {
    this.sortLanguage = sortLanguage;
  }
}
