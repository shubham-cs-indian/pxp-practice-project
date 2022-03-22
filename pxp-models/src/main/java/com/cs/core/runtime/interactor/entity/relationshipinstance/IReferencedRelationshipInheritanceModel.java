package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import java.util.List;

public interface IReferencedRelationshipInheritanceModel extends IReferencedRelationshipModel {
  
  String SIDE1_KLASS_IDS    = "side1KlassIds";
  String SIDE2_KLASS_IDS    = "side2KlassIds";
  String SIDE1_TAXONOMY_IDS = "side1TaxonomyIds";
  String SIDE2_TAXONOMY_IDS = "side2TaxonomyIds";
  // String CONFLICTING_SOURCES = "conflictingSources";
  
  public List<String> getSide1KlassIds();
  
  public void setSide1KlassIds(List<String> side1KlassIds);
  
  public List<String> getSide2KlassIds();
  
  public void setSide2KlassIds(List<String> side2KlassIds);
  
  public List<String> getSide1TaxonomyIds();
  
  public void setSide1TaxonomyIds(List<String> side1TaxonomyIds);
  
  public List<String> getSide2TaxonomyIds();
  
  public void setSide2TaxonomyIds(List<String> side2TaxonomyIds);
  
  /* public Map<String, IIdAndCouplingTypeModel> getConflictingSources();
  public void setConflictingSources(Map<String, IIdAndCouplingTypeModel> conflictingSources);*/
}
