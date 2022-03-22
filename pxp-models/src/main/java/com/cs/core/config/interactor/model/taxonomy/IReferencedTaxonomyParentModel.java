package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IReferencedTaxonomyParentModel extends IModel {
  
  public static final String ID            = "id";
  public static final String LABEL         = "label";
  public static final String CODE          = "code";
  public static final String ICON          = "icon";
  public static final String PARENT        = "parent";
  public static final String TAXONOMY_TYPE = "taxonomyType";
  public static final String BASE_TYPE     = "baseType";
  public static final String ICON_KEY      = "iconKey";
  
  public String getId();
  
  public void setId(String id);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public IReferencedTaxonomyParentModel getParent();
  
  public void setParent(IReferencedTaxonomyParentModel icon);
  
  public String getTaxonomyType();
  
  public void setTaxonomyType(String taxonomyType);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getIconKey();
  
  public void setIconKey(String iconKey);
}
