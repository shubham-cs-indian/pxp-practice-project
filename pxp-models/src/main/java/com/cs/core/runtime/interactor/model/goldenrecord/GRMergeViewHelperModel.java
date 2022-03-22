package com.cs.core.runtime.interactor.model.goldenrecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GRMergeViewHelperModel implements IGRMergeViewHelperModel {
  
  private static final long serialVersionUID = 1L;
 
  protected Map<String, List<ITagRecommendationModel>>                    supplierTags                      = new HashMap<>();
  protected Map<String, List<IAttributeRecommendationModel>>              supplierAttributes                = new HashMap<>();
  protected Map<String, Map<String, List<IAttributeRecommendationModel>>> supplierDependentAttributes       = new HashMap<>();
  protected Map<String, List<ITagRecommendationModel>>                    lastModifiedTags                  = new HashMap<>();
  protected Map<String, List<IAttributeRecommendationModel>>              lastModifiedAttributes            = new HashMap<>();
  protected Map<String, Map<String, List<IAttributeRecommendationModel>>> lastModifiedDependentAttributes   = new HashMap<>();
  protected Map<String, Map<String, IRecommendationModel>>                dependentAttributesRecommendation = new HashMap<>();
  
  @Override
  public void setSupplierTags(Map<String, List<ITagRecommendationModel>> supplierTags)
  {
    this.supplierTags = supplierTags;
  }
  
  @Override
  public Map<String, List<ITagRecommendationModel>> getSupplierTags()
  {
    return this.supplierTags;
  }
  
  @Override
  public void setSupplierAttributes(
      Map<String, List<IAttributeRecommendationModel>> supplierAttributes)
  {
    this.supplierAttributes = supplierAttributes;
  }
  
  @Override
  public Map<String, List<IAttributeRecommendationModel>> getSupplierAttributes()
  {
    return this.supplierAttributes;
  }
  
  @Override
  public void setSupplierDependentAttributes(
      Map<String, Map<String, List<IAttributeRecommendationModel>>> supplierDependentAttributes)
  {
    this.supplierDependentAttributes = supplierDependentAttributes;
  }
  
  @Override
  public Map<String, Map<String, List<IAttributeRecommendationModel>>> getSupplierDependentAttributes()
  {
    return this.supplierDependentAttributes;
  }
  
  @Override
  public void setLastModifiedTags(Map<String, List<ITagRecommendationModel>> lastModifiedTags)
  {
    this.lastModifiedTags = lastModifiedTags;
  }
  
  @Override
  public Map<String, List<ITagRecommendationModel>> getLastModifiedTags()
  {
    return this.lastModifiedTags;
  }
  
  @Override
  public void setLastModifiedAttributes(
      Map<String, List<IAttributeRecommendationModel>> lastModifiedAttributes)
  {
    this.lastModifiedAttributes = lastModifiedAttributes;
  }
  
  @Override
  public Map<String, List<IAttributeRecommendationModel>> getLastModifiedAttributes()
  {
    return this.lastModifiedAttributes;
  }
  
  @Override
  public void setLastModifiedDependentAttributes(
      Map<String, Map<String, List<IAttributeRecommendationModel>>> lastModifiedDependentAttributes)
  {
    this.lastModifiedDependentAttributes = lastModifiedDependentAttributes;
  }
  
  @Override
  public Map<String, Map<String, List<IAttributeRecommendationModel>>> getLastModifiedDependentAttributes()
  {
    return this.lastModifiedDependentAttributes;
  }
  
  @Override
  public void setDependentAttributesRecommendation(
      Map<String, Map<String, IRecommendationModel>> dependentAttributesRecommendation)
  {
    this.dependentAttributesRecommendation = dependentAttributesRecommendation;
  }
  
  @Override
  public Map<String, Map<String, IRecommendationModel>> getDependentAttributesRecommendation()
  {
    return this.dependentAttributesRecommendation;
  }
  
}
