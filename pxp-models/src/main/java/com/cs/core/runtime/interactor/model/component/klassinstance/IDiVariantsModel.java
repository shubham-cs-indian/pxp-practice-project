package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.component.jms.IDiModel;

import java.util.List;

public interface IDiVariantsModel extends IDiModel {
  
  public static String VARIANT_ID     = "variantId";
  public static String PARENT_ID      = "parentId";
  public static String KLASS_TYPE_IDS = "klassTypeIds";
  public static String PROPERTIES     = "properties";
  
  public String getVariantId();
  
  public void setVariantId(String variantId);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public List<String> getKlassTypeIds();
  
  public void setKlassTypeIds(List<String> klassTypeIds);
  
  public IDiPropertiesModel getProperties();
  
  public void setProperties(IDiPropertiesModel properties);
}
