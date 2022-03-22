package com.cs.core.runtime.interactor.model.variants;

import java.util.ArrayList;
import java.util.List;

public class VersionMatchAndMergeDataRequestModel implements IVersionMatchAndMergeDataRequestModel {
  
  private static final long serialVersionUID          = 1L;
  protected String          id;
  protected List<String>    propagablePropertyIds;
  protected List<String>    variantContextIds;
  protected List<String>    referencedRelationshipIds = new ArrayList<>();
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<String> getPropagablePropertyIds()
  {
    return propagablePropertyIds;
  }
  
  @Override
  public void setPropagablePropertyIds(List<String> propagablePropertyIds)
  {
    this.propagablePropertyIds = propagablePropertyIds;
  }
  
  @Override
  public List<String> getVariantContextIds()
  {
    return variantContextIds;
  }
  
  @Override
  public void setVariantContextIds(List<String> variantContextIds)
  {
    this.variantContextIds = variantContextIds;
  }
  
  @Override
  public List<String> getReferencedRelationshipIds()
  {
    return referencedRelationshipIds;
  }
  
  @Override
  public void setReferencedRelationshipIds(List<String> referencedRelationshipIds)
  {
    this.referencedRelationshipIds = referencedRelationshipIds;
  }
}
