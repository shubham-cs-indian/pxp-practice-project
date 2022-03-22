package com.cs.core.runtime.interactor.model.dataintegration;

public interface IStandardAckBodyModel extends IAcknowledgementBodyModel {
  
  public static final String SUMMARY             = "summary";
  public static final String CONTENT             = "content";
  public static final String EMBEDDEDVARIANTS    = "embeddedVariants";
  public static final String RELATIONSHIPS       = "relationships";
  public static final String NATURERELATIONSHIPS = "natureRelationships";
  public static final String ASSETS              = "assets";
  public static final String MARKETS             = "markets";
  public static final String SUPPLIERS           = "suppliers";
  public static final String TAXONOMIES          = "taxonomies";
  
  public IDiSuccessFailureCountModel getSummary();
  
  public void setSummary(IDiSuccessFailureCountModel summary);
  
  public IDiSuccessFailureForAckBodyModel getContent();
  
  public void setContent(IDiSuccessFailureForAckBodyModel content);
  
  public IDiSuccessFailureForAckBodyModel getEmbeddedVariants();
  
  public void setEmbeddedVariants(IDiSuccessFailureForAckBodyModel embeddedVariants);
  
  public IDiSuccessFailureForAckBodyModel getRelationships();
  
  public void setRelationships(IDiSuccessFailureForAckBodyModel relationships);
  
  public IDiSuccessFailureForAckBodyModel getNatureRelationships();
  
  public void setNatureRelationships(IDiSuccessFailureForAckBodyModel natureRelationships);
  
  public IDiSuccessFailureForAckBodyModel getAssets();
  
  public void setAssets(IDiSuccessFailureForAckBodyModel assets);
  
  public IDiSuccessFailureForAckBodyModel getMarkets();
  
  public void setMarkets(IDiSuccessFailureForAckBodyModel markets);
  
  public IDiSuccessFailureForAckBodyModel getSuppliers();
  
  public void setSuppliers(IDiSuccessFailureForAckBodyModel suppliers);
  
  public IDiSuccessFailureForAckBodyModel getTaxonomies();
  
  public void setTaxonomies(IDiSuccessFailureForAckBodyModel taxonomies);
}
