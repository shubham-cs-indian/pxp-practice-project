package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.variantcontext.IAttributeContext;

public interface ISectionAttribute extends ISectionElement {
  
  public static final String ATTRIBUTE               = "attribute";
  public static final String NUMBER_OF_ITEMS_ALLOWED = "numberOfItemsAllowed";
  public static final String DEFAULT_VALUE           = "defaultValue";
  public static final String PRECISION               = "precision";
  public static final String DEFAULT_UNIT            = "defaultUnit";
  public static final String CONTEXT                 = "context";
  public static final String IS_IDENTIFIER           = "isIdentifier";
  public static final String VALUE_AS_HTML           = "valueAsHtml";
  public static final String PREFIX                  = "prefix";
  public static final String SUFFIX                  = "suffix";
  public static final String IS_VERSIONABLE          = "isVersionable";
  
  public IAttribute getAttribute();
  
  public void setAttribute(IAttribute attribute);
  
  public Integer getNumberOfItemsAllowed();
  
  public void setNumberOfItemsAllowed(Integer numberOfItemsAllowed);
  
  public String getDefaultValue();
  
  public void setDefaultValue(String defaultValue);
  
  public Integer getPrecision();
  
  public void setPrecision(Integer precision);
  
  public String getDefaultUnit();
  
  public void setDefaultUnit(String defaultUnit);
  
  public IAttributeContext getContext();
  
  public void setContext(IAttributeContext context);
  
  public Boolean getIsIdentifier();
  
  public void setIsIdentifier(Boolean isIdentifier);
  
  public String getValueAsHtml();
  
  public void setValueAsHtml(String valueAsHtml);
  
  public String getPrefix();
  
  public void setPrefix(String prefix);
  
  public String getSuffix();
  
  public void setSuffix(String suffix);
  
  public Boolean getIsVersionable();
  
  public void setIsVersionable(Boolean isVersionable);
}
