package com.cs.core.runtime.interactor.model.offboarding;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICustomExportParameterModel extends IModel {
  
  public static String ATTRIBUTE_MAP_VALUES           = "attributeMapValues";
  public static String TAG_MAP_VALUES                 = "tagMapValues";
  public static String KLASS_MAP_VALUES               = "klassMapValues";
  public static String TAXONOMY_MAP_VALUES            = "taxonomyMapValues";
  public static String PRODUCT_LIST                   = "productList";
  public static String EMBEDDED_VARIANT_ARTICLE_MAP   = "embeddedVariantArticleMap";
  public static String ALL_NATURE_KLASS_IDS           = "allNatureKlassIds";
  public static String SUCCESS_IDS                    = "successIds";
  public static String HEADER                         = "header";
  public static String IS_MULTICLASSIFICATION_ENABLED = "isMultiClassEnabled";
  
  public Map<String, String> getAttributeMapValues();
  
  public void setAttributeMapValues(Map<String, String> attributeMapValues);
  
  public Map<String, String> getTagMapValues();
  
  public void setTagMapValues(Map<String, String> tagMapValues);
  
  public Map<String, String> getKlassMapValues();
  
  public void setKlassMapValues(Map<String, String> klassMapValues);
  
  public Map<String, String> getTaxonomyMapValues();
  
  public void setTaxonomyMapValues(Map<String, String> taxonomyMapValues);
  
  public List<String> getProductList();
  
  public void setProductList(List<String> productList);
  
  public Map<String, Set<String>> getEmbeddedVariantArticleMap();
  
  public void setEmbeddedVariantArticleMap(Map<String, Set<String>> embeddedVariantArticleMap);
  
  public List<String> getAllNatureKlassIds();
  
  public void setAllNatureKlassIds(List<String> allNatureKlassIds);
  
  public List<String> getSuccessIds();
  
  public void setSuccessIds(List<String> successIds);
  
  public LinkedHashSet<String> getHeader();
  
  public void setHeader(LinkedHashSet<String> header);
  
  public Boolean getIsMultiClassEnabled();
  
  public void setIsMultiClassEnabled(Boolean isMultiClassEnabled);
  
  public Map<String, Object> getStandardHeaders();
  
  public void setStandardHeaders(Map<String, Object> standardHeaders);
  
  public List<String> getExcludedAttributes();
  
  public void setExcludedAttributes(List<String> excludedAttributes);
}
