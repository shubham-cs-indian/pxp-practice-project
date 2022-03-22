package com.cs.core.config.interactor.model.variantcontext;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class ReferencedContextModel implements IReferencedContextModel {
  
  private static final long                             serialVersionUID            = 1L;
  protected Map<String, IReferencedVariantContextModel> embeddedVariantContexts     = new HashMap<>();
  protected Map<String, IReferencedVariantContextModel> productVariantContexts      = new HashMap<>();
  protected Map<String, IReferencedVariantContextModel> languageVariantContexts     = new HashMap<>();
  protected Map<String, IReferencedVariantContextModel> promotionalVariantContexts  = new HashMap<>();
  protected Map<String, IReferencedVariantContextModel> relationshipVariantContexts = new HashMap<>();
  
  @Override
  public Map<String, IReferencedVariantContextModel> getEmbeddedVariantContexts()
  {
    
    return embeddedVariantContexts;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedVariantContextModel.class)
  public void setEmbeddedVariantContexts(
      Map<String, IReferencedVariantContextModel> embeddedVariantContexts)
  {
    this.embeddedVariantContexts = embeddedVariantContexts;
  }
  
  @Override
  public Map<String, IReferencedVariantContextModel> getProductVariantContexts()
  {
    
    return productVariantContexts;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedVariantContextModel.class)
  public void setProductVariantContexts(
      Map<String, IReferencedVariantContextModel> productVariantContexts)
  {
    this.productVariantContexts = productVariantContexts;
  }
  
  @Override
  public Map<String, IReferencedVariantContextModel> getLanguageVariantContexts()
  {
    
    return languageVariantContexts;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedVariantContextModel.class)
  public void setLanguageVariantContexts(
      Map<String, IReferencedVariantContextModel> languageVariantContexts)
  {
    this.languageVariantContexts = languageVariantContexts;
  }
  
  @Override
  public Map<String, IReferencedVariantContextModel> getPromotionalVersionContexts()
  {
    
    return promotionalVariantContexts;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedVariantContextModel.class)
  public void setPromotionalVersionContexts(
      Map<String, IReferencedVariantContextModel> promotionalVariantContexts)
  {
    this.promotionalVariantContexts = promotionalVariantContexts;
  }
  
  @Override
  public Map<String, IReferencedVariantContextModel> getRelationshipVariantContexts()
  {
    return relationshipVariantContexts;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedVariantContextModel.class)
  public void setRelationshipVariantContexts(
      Map<String, IReferencedVariantContextModel> relationshipVariantContexts)
  {
    this.relationshipVariantContexts = relationshipVariantContexts;
  }
  
}
