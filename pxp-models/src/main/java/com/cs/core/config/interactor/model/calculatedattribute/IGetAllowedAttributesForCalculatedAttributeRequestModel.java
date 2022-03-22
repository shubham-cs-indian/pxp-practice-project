package com.cs.core.config.interactor.model.calculatedattribute;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetAllowedAttributesForCalculatedAttributeRequestModel extends IModel {
  
  public static final String FROM                      = "from";
  public static final String SIZE                      = "size";
  public static final String SORT_BY                   = "sortBy";
  public static final String SORT_ORDER                = "sortOrder";
  public static final String SEARCH_COLUMN             = "searchColumn";
  public static final String SEARCH_TEXT               = "searchText";
  public static final String CALCULATED_ATTRIBUTE_TYPE = "calculatedAttributeType";
  public static final String CALCULATED_ATTRIBUTE_UNIT = "calculatedAttributeUnit";
  public static final String SHOULD_ALLOW_SELF         = "shouldAllowSelf";
  public static final String MAPPING                   = "mapping";
  public static final String ATTRIBUTE_ID              = "attributeId";
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getSize();
  
  public void setSize(Long size);
  
  public String getSortBy();
  
  public void setSortBy(String sortBy);
  
  public String getSortOrder();
  
  public void setSortOrder(String sortOrder);
  
  public String getSearchColumn();
  
  public void setSearchColumn(String searchColumn);
  
  public String getSearchText();
  
  public void setSearchText(String searchText);
  
  public String getCalculatedAttributeType();
  
  public void setCalculatedAttributeType(String calculatedAttributeType);
  
  public String getCalculatedAttributeUnit();
  
  public void setCalculatedAttributeUnit(String calculatedAttributeUnit);
  
  public Boolean getShouldAllowSelf();
  
  public void getShouldAllowSelf(Boolean shouldAllowSelf);
  
  public Map<String, Map<String, Map<String, String>>> getMapping();
  
  public void setMapping(Map<String, Map<String, Map<String, String>>> mapping);
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
}
