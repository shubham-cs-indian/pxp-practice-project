package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IKlassContextModel extends IModel {
  
  public static final String EMBEDDED_VARIANT_CONTEXTS    = "embeddedVariantContexts";
  public static final String PRODUCT_VARIANT_CONTEXTS     = "productVariantContexts";
  public static final String LANGUAGE_VARIANT_CONTEXTS    = "languageVariantContexts";
  public static final String PROMOTIONAL_VERSION_CONTEXTS = "promotionalVersionContexts";
  
  public List<String> getEmbeddedVariantContexts();
  
  public void setEmbeddedVariantContexts(List<String> embeddedVariantContexts);
  
  public List<String> getProductVariantContexts();
  
  public void setProductVariantContexts(List<String> productVariantContexts);
  
  public List<String> getLanguageVariantContexts();
  
  public void setLanguageVariantContexts(List<String> languageVariantContexts);
  
  public List<String> getPromotionalVersionContexts();
  
  public void setPromotionalVersionContexts(List<String> promotionalVersionContexts);

}
