package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetTranslationsRequestModel extends IModel {
  
  public static final String ENTITY_TYPE     = "entityType";
  public static final String LANGUAGES       = "languages";
  public static final String FROM            = "from";
  public static final String SIZE            = "size";
  public static final String SEARCH_LANGUAGE = "searchLanguage";
  public static final String SEARCH_TEXT     = "searchText";
  public static final String SEARCH_FIELD    = "searchField";
  public static final String SORT_LANGUAGE   = "sortLanguage";
  public static final String SORT_ORDER      = "sortOrder";
  public static final String SORT_BY         = "sortBy";
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
  
  public List<String> getLanguages();
  
  public void setLanguages(List<String> languages);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public String getSearchLanguage();
  
  public void setSearchLanguage(String searchLanguage);
  
  public String getSearchText();
  
  public void setSearchText(String searchText);
  
  public String getSearchField();
  
  public void setSearchField(String searchField);
  
  public String getSortBy();
  
  public void setSortBy(String sortBy);
  
  public String getSortOrder();
  
  public void setSortOrder(String sortOrder);
  
  public String getSortLanguage();
  
  public void setSortLanguage(String sortLanguage);
}
