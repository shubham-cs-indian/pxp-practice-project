package com.cs.core.config.interactor.model.transfer;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGetVariantsForArticleResponseModel extends IModel {
  
  public static final String BATCH_ID               = "batchId";
  public static final String CONFIG_DETAILS         = "configDetails";
  public static final String SOURCE_DESTINATION_IDS = "sourceDestinationIds";
  public static final String EMBEDDED_VARIANT_IDS   = "embeddedVariantIds";
  public static final String ATTRIBUTE_VARIANT_IDS  = "attributeVariantIds";
  public static final String KLASS_IDS              = "klassIds";
  public static final String TAXONOMY_IDS           = "taxonomyIds";
  public static final String PARENT_ID              = "parentId";
  public static final String KLASS_INSTANCE_ID      = "klassInstanceId";
  public static final String PATH                   = "path";
  public static final String VARIANT_INSTANCE_ID    = "variantInstanceId";
  
  public String getBatchId();
  
  public void setBatchId(String batchId);
  
  public IGetConfigDetailsForCustomTabModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails);
  
  public Map<String, String> getSourceDestinationIds();
  
  public void setSourceDestinationIds(Map<String, String> sourceDestinationIds);
  
  public Set<String> getTaxonomyIds();
  
  public void setTaxonomyIds(Set<String> taxonomyIds);
  
  public Set<String> getKlassIds();
  
  public void setKlassIds(Set<String> klassIds);
  
  public Set<String> getPath();
  
  public void setPath(Set<String> path);
  
  public List<String> getEmbeddedVariantIds();
  
  public void setEmbeddedVariantIds(List<String> embeddedVariantIds);
  
  public List<String> getAttributeVariantIds();
  
  public void setAttributeVariantIds(List<String> attributeVariantIds);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String klassInstanceId);
}
