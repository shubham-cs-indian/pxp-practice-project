package com.cs.core.runtime.interactor.model.dataintegration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class StandardAckBodyModel extends AcknowledgementBodyModel
    implements IStandardAckBodyModel {
  
  private static final long               serialVersionUID = 1L;
  public IDiSuccessFailureCountModel      summary;
  public IDiSuccessFailureForAckBodyModel content;
  public IDiSuccessFailureForAckBodyModel embeddedVariants;
  public IDiSuccessFailureForAckBodyModel relationships;
  public IDiSuccessFailureForAckBodyModel natureRelationships;
  public IDiSuccessFailureForAckBodyModel assets;
  public IDiSuccessFailureForAckBodyModel markets;
  public IDiSuccessFailureForAckBodyModel suppliers;
  public IDiSuccessFailureForAckBodyModel taxonomies;
  
  @Override
  public IDiSuccessFailureCountModel getSummary()
  {
    
    return this.summary;
  }
  
  @Override
  @JsonDeserialize(as = DiSuccessFailureCountModel.class)
  public void setSummary(IDiSuccessFailureCountModel summary)
  {
    this.summary = summary;
  }
  
  @Override
  public IDiSuccessFailureForAckBodyModel getContent()
  {
    return content;
  }
  
  @Override
  @JsonDeserialize(as = DiSuccessFailureForAckBodyModel.class)
  public void setContent(IDiSuccessFailureForAckBodyModel content)
  {
    this.content = content;
  }
  
  @Override
  public IDiSuccessFailureForAckBodyModel getEmbeddedVariants()
  {
    return embeddedVariants;
  }
  
  @Override
  @JsonDeserialize(as = DiSuccessFailureForAckBodyModel.class)
  public void setEmbeddedVariants(IDiSuccessFailureForAckBodyModel embeddedVariants)
  {
    this.embeddedVariants = embeddedVariants;
  }
  
  @Override
  public IDiSuccessFailureForAckBodyModel getRelationships()
  {
    return relationships;
  }
  
  @Override
  @JsonDeserialize(as = DiSuccessFailureForAckBodyModel.class)
  public void setRelationships(IDiSuccessFailureForAckBodyModel relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public IDiSuccessFailureForAckBodyModel getNatureRelationships()
  {
    return natureRelationships;
  }
  
  @Override
  @JsonDeserialize(as = DiSuccessFailureForAckBodyModel.class)
  public void setNatureRelationships(IDiSuccessFailureForAckBodyModel natureRelationships)
  {
    this.natureRelationships = natureRelationships;
  }
  
  @Override
  public IDiSuccessFailureForAckBodyModel getAssets()
  {
    
    return assets;
  }
  
  @Override
  @JsonDeserialize(as = DiSuccessFailureForAckBodyModel.class)
  public void setAssets(IDiSuccessFailureForAckBodyModel asset)
  {
    this.assets = asset;
  }
  
  @Override
  public IDiSuccessFailureForAckBodyModel getMarkets()
  {
    return markets;
  }
  
  @Override
  @JsonDeserialize(as = DiSuccessFailureForAckBodyModel.class)
  public void setMarkets(IDiSuccessFailureForAckBodyModel market)
  {
    this.markets = market;
  }
  
  @Override
  public IDiSuccessFailureForAckBodyModel getSuppliers()
  {
    return suppliers;
  }
  
  @Override
  @JsonDeserialize(as = DiSuccessFailureForAckBodyModel.class)
  public void setSuppliers(IDiSuccessFailureForAckBodyModel supplier)
  {
    this.suppliers = supplier;
  }
  
  @Override
  public IDiSuccessFailureForAckBodyModel getTaxonomies()
  {
    return taxonomies;
  }
  
  @Override
  @JsonDeserialize(as = DiSuccessFailureForAckBodyModel.class)
  public void setTaxonomies(IDiSuccessFailureForAckBodyModel taxonomies)
  {
    this.taxonomies = taxonomies;
  }
}
