package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IReferencedContextModel extends IModel {
  
  public static final String EMBEDDED_VARIANT_CONTEXTS     = "embeddedVariantContexts";
  public static final String PRODUCT_VARIANT_CONTEXTS      = "productVariantContexts";
  public static final String LANGUAGE_VARIANT_CONTEXTS     = "languageVariantContexts";
  public static final String PROMOTIONAL_VERSION_CONTEXTS  = "promotionalVersionContexts";
  public static final String RELATIONSHIP_VARIANT_CONTEXTS = "relationshipVariantContexts";
  public static final String VERSION_CONTEXTS              = "versionContexts";
  
  public Map<String, IReferencedVariantContextModel> getEmbeddedVariantContexts();
  
  public void setEmbeddedVariantContexts(
      Map<String, IReferencedVariantContextModel> embeddedVariantContexts);
  
  public Map<String, IReferencedVariantContextModel> getProductVariantContexts();
  
  public void setProductVariantContexts(
      Map<String, IReferencedVariantContextModel> productVariantContexts);
  
  public Map<String, IReferencedVariantContextModel> getLanguageVariantContexts();
  
  public void setLanguageVariantContexts(
      Map<String, IReferencedVariantContextModel> languageVariantContexts);
  
  public Map<String, IReferencedVariantContextModel> getPromotionalVersionContexts();
  
  public void setPromotionalVersionContexts(
      Map<String, IReferencedVariantContextModel> promotionalVariantContexts);
  
  public Map<String, IReferencedVariantContextModel> getRelationshipVariantContexts();
  
  public void setRelationshipVariantContexts(
      Map<String, IReferencedVariantContextModel> relationshipVariantContexts);

}
