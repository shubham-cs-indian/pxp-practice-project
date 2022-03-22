package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

public class KlassContextModel implements IKlassContextModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    embeddedVariantContexts;
  protected List<String>    productVariantContexts;
  protected List<String>    languageVariantContexts;
  protected List<String>    promotionalVersionContexts;
  
  @Override
  public List<String> getEmbeddedVariantContexts()
  {
    
    return embeddedVariantContexts;
  }
  
  @Override
  public void setEmbeddedVariantContexts(List<String> embeddedVariantContexts)
  {
    this.embeddedVariantContexts = embeddedVariantContexts;
  }
  
  @Override
  public List<String> getProductVariantContexts()
  {
    
    return productVariantContexts;
  }
  
  @Override
  public void setProductVariantContexts(List<String> productVariantContexts)
  {
    this.productVariantContexts = productVariantContexts;
  }
  
  @Override
  public List<String> getLanguageVariantContexts()
  {
    
    return languageVariantContexts;
  }
  
  @Override
  public void setLanguageVariantContexts(List<String> languageVariantContexts)
  {
    this.languageVariantContexts = languageVariantContexts;
  }
  
  @Override
  public List<String> getPromotionalVersionContexts()
  {
    
    return promotionalVersionContexts;
  }
  
  @Override
  public void setPromotionalVersionContexts(List<String> promotionalVariantContexts)
  {
    this.promotionalVersionContexts = promotionalVariantContexts;
  }
}
