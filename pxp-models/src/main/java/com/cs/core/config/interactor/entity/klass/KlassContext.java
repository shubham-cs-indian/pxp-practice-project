package com.cs.core.config.interactor.entity.klass;

import java.util.List;

public class KlassContext implements IKlassContext {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    embeddedVariantContexts;
  protected List<String>    productVariantContexts;
  protected List<String>    languageVariantContexts;
  protected List<String>    promotionalVariantContexts;
  
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
    
    return promotionalVariantContexts;
  }
  
  @Override
  public void setPromotionalVersionContexts(List<String> promotionalVariantContexts)
  {
    this.promotionalVariantContexts = promotionalVariantContexts;
  }
  
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
