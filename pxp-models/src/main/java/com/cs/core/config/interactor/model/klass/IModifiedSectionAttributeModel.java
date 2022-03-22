package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.variantcontext.IModifiedContextModel;

public interface IModifiedSectionAttributeModel extends IModifiedSectionElementModel {
  
  public static final String NUMBER_OF_ITEMS_ALLOWED = "numberOfItemsAllowed";
  public static final String DEFAULT_VALUE           = "defaultValue";
  public static final String DEFAULT_UNIT            = "defaultUnit";
  public static final String PRECISION               = "precision";
  public static final String CONTEXT                 = "context";
  public static final String IS_IDENTIFIER           = "isIdentifier";
  public static final String VALUE_AS_HTML           = "valueAsHtml";
  public static final String PREFIX                  = "prefix";
  public static final String SUFFIX                  = "suffix";
  public static final String IS_VERSIONABLE          = "isVersionable";
  
  public String getDefaultValue();
  
  public void setDefaultValue(String defaultValue);
  
  public Integer getNumberOfItemsAllowed();
  
  public void setNumberOfItemsAllowed(Integer numberOfItemsAllowed);
  
  public Integer getPrecision();
  
  public void setPrecision(Integer precision);
  
  public String getDefaultUnit();
  
  public void setDefaultUnit(String defaultUnit);
  
  public IModifiedContextModel getContext();
  
  public void setContext(IModifiedContextModel context);
  
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
