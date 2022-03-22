package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.component.jms.IDiModel;

import java.util.List;
import java.util.Map;

public interface IDiKlassInstanceImportMessageModel extends IDiModel {
  
  public static String CONTENT              = "content";
  public static String EMBEDDED_VARIANTS    = "embeddedVariants";
  public static String RELATIONSHIPS        = "relationships";
  public static String NATURE_RELATIONSHIPS = "natureRelationships";
  public static String ASSETS               = "assets";
  public static String SUPPLIERS            = "suppliers";
  public static String MARKETS              = "markets";
  
  public List<Map<String, Object>> getContent();
  
  public void setContent(List<Map<String, Object>> content);
  
  public List<IDiEmbeddedVariantsModel> getEmbeddedVariants();
  
  public void setEmbeddedVariants(List<IDiEmbeddedVariantsModel> embeddedVariants);
  
  public List<IDiRelationshipsModel> getRelationships();
  
  public void setRelationships(List<IDiRelationshipsModel> relationships);
  
  public List<IDiRelationshipsModel> getNatureRelationships();
  
  public void setNatureRelationships(List<IDiRelationshipsModel> natureRelationships);
  
  public List<Map<String, Object>> getAssets();
  
  public void setAssets(List<Map<String, Object>> assets);
  
  public List<Map<String, Object>> getSuppliers();
  
  public void setSuppliers(List<Map<String, Object>> suppliers);
  
  public List<Map<String, Object>> getMarkets();
  
  public void setMarkets(List<Map<String, Object>> markets);
}
