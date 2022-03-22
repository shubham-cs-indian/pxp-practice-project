package com.cs.core.config.interactor.model.matchandmerge;

import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IPropertyInfoModel;

import java.util.List;

public interface IMatchAndMergeConfigDetailsRequestModel extends IMulticlassificationRequestModel {
  
  String MATCH_RELATIONSHIP_IDS   = "relationshipIds";
  String MATCH_REFERENCE_IDS      = "referenceIds";
  String MATCH_RELATIONSHIP_TYPES = "matchRelationshipTypes";
  String MATCH_REFERENCE_TYPES    = "matchReferenceTypes";
  String PROPERTY_INFO_MODEL      = "propertyInfoModel";
  
  public List<String> getMatchRelationshipTypes();
  
  public void setMatchRelationshipTypes(List<String> matchRelationshipTypes);
  
  public List<String> getMatchReferenceTypes();
  
  public void setMatchReferenceTypes(List<String> matchReferenceTypes);
  
  public List<String> getRelationshipIds();
  
  public void setRelationshipIds(List<String> matchRelationshipIds);
  
  public List<String> getReferenceIds();
  
  public void setReferenceIds(List<String> matchReferenceIds);
  
  public IPropertyInfoModel getPropertyInfoModel();
  
  public void setPropertyInfoModel(IPropertyInfoModel propertyInfoModel);
}
