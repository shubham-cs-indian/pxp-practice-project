package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;

import java.util.List;

public interface IVariantFilterRequestModel extends IModel {
  
  public static final String ATTRIBUTES   = "attributes";
  public static final String TAGS         = "tags";
  public static final String IS_RED       = "isRed";
  public static final String IS_ORANGE    = "isOrange";
  public static final String IS_YELLOW    = "isYellow";
  public static final String IS_GREEN     = "isGreen";
  public static final String ALL_SEARCH   = "allSearch";
  public static final String SORT_OPTIONS = "sortOptions";
  public static final String FROM         = "from";
  public static final String SIZE         = "size";
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public List<? extends IPropertyInstanceFilterModel> getAttributes();
  
  public void setAttributes(List<? extends IPropertyInstanceFilterModel> attributes);
  
  public List<? extends IPropertyInstanceFilterModel> getTags();
  
  public void setTags(List<? extends IPropertyInstanceFilterModel> tags);
  
  public List<ISortModel> getSortOptions();
  
  public void setSortOptions(List<ISortModel> sortOptions);
  
  public Boolean getIsRed();
  
  public void setIsRed(Boolean isRed);
  
  public Boolean getIsOrange();
  
  public void setIsOrange(Boolean isOrange);
  
  public Boolean getIsYellow();
  
  public void setIsYellow(Boolean isYellow);
  
  public Boolean getIsGreen();
  
  public void setIsGreen(Boolean isGreen);
}
