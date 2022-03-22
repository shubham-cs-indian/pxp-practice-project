package com.cs.core.runtime.interactor.entity.relationshipinstance;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.relationship.ReferencedRelationshipModel;

public class ReferencedRelationshipInheritanceModel extends ReferencedRelationshipModel implements IReferencedRelationshipInheritanceModel{
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>                         side1KlassIds;
  protected List<String>                         side2KlassIds;
  protected List<String>                         side1TaxonomyIds;
  protected List<String>                         side2TaxonomyIds;

  @Override
  public List<String> getSide1KlassIds()
  {
    if(side1KlassIds == null) {
      side1KlassIds = new ArrayList<>();
    }
    return side1KlassIds;
  }
  
  @Override
  public void setSide1KlassIds(List<String> side1KlassIds)
  {
    this.side1KlassIds = side1KlassIds;
  }
  
  @Override
  public List<String> getSide2KlassIds()
  {
    if(side2KlassIds == null) {
      side2KlassIds = new ArrayList<>();
    }
    return side2KlassIds;
  }
  
  @Override
  public void setSide2KlassIds(List<String> side2KlassIds)
  {
    this.side2KlassIds = side2KlassIds;
  }
  
  @Override
  public List<String> getSide1TaxonomyIds()
  {
    if(side1TaxonomyIds == null) {
      side1TaxonomyIds = new ArrayList<>();
    }
    return side1TaxonomyIds;
  }
  
  @Override
  public void setSide1TaxonomyIds(List<String> side1TaxonomyIds)
  {
    this.side1TaxonomyIds = side1TaxonomyIds;
  }
  
  @Override
  public List<String> getSide2TaxonomyIds()
  {
    if(side2TaxonomyIds == null) {
      side2TaxonomyIds = new ArrayList<>();
    }
    return side2TaxonomyIds;
  }
  
  @Override
  public void setSide2TaxonomyIds(List<String> side2TaxonomyIds)
  {
    this.side2TaxonomyIds = side2TaxonomyIds;
  }
  
}
