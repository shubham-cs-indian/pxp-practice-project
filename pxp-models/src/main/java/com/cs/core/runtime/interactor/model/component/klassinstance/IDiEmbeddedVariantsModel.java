package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IDiEmbeddedVariantsModel extends IModel {
  
  public static String VARIANT_ID     = "variantId";
  public static String CONTENT_ID     = "contentId";
  public static String PARENT_ID      = "parentId";
  public static String TYPE_IDS       = "typeIds";
  public static String TAXONOMIES_IDS = "taxonomyIds";
  public static String CONTEXT        = "context";
  public static String PROPERTIES     = "properties";
  public static String LABEL          = "label";
  public static String LANGUAGE_CODE  = "languageCode";
  
  public String getVariantId();
  
  public void setVariantId(String variantId);
  
  public String getContentId();
  
  public void setContentId(String klassInstanceId);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public List<String> getTypeIds();
  
  public void setTypeIds(List<String> typeIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public IDiAttributeVariantContextModel getContext();
  
  public void setContext(IDiAttributeVariantContextModel context);
  
  public IDiPropertiesModel getProperties();
  
  public void setProperties(IDiPropertiesModel properties);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getLanguageCode();
  
  public void setLanguageCode(String languageCode);
}
