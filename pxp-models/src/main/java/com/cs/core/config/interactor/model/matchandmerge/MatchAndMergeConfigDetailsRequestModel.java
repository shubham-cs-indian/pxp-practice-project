package com.cs.core.config.interactor.model.matchandmerge;

import com.cs.core.runtime.interactor.model.goldenrecord.IPropertyInfoModel;
import com.cs.core.runtime.interactor.model.goldenrecord.PropertyInfoModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class MatchAndMergeConfigDetailsRequestModel extends MulticlassificationRequestModel
    implements IMatchAndMergeConfigDetailsRequestModel {
  
  private static final long    serialVersionUID = 1L;
  
  protected List<String>       relationshipIds;
  protected List<String>       referenceIds;
  protected List<String>       matchRelationshipTypes;
  protected List<String>       matchReferenceTypes;
  protected IPropertyInfoModel propertyInfoModel;
  
  @Override
  public List<String> getRelationshipIds()
  {
    if (relationshipIds == null) {
      relationshipIds = new ArrayList<>();
    }
    return relationshipIds;
  }
  
  @Override
  public void setRelationshipIds(List<String> relationshipIds)
  {
    this.relationshipIds = relationshipIds;
  }
  
  @Override
  public List<String> getReferenceIds()
  {
    if (referenceIds == null) {
      referenceIds = new ArrayList<>();
    }
    return referenceIds;
  }
  
  @Override
  public void setReferenceIds(List<String> referenceIds)
  {
    this.referenceIds = referenceIds;
  }
  
  @Override
  public List<String> getMatchRelationshipTypes()
  {
    if (matchRelationshipTypes == null) {
      matchRelationshipTypes = new ArrayList<>();
    }
    return matchRelationshipTypes;
  }
  
  @Override
  public void setMatchRelationshipTypes(List<String> matchRelationshipTypes)
  {
    this.matchRelationshipTypes = matchRelationshipTypes;
  }
  
  @Override
  public List<String> getMatchReferenceTypes()
  {
    if (matchReferenceTypes == null) {
      matchReferenceTypes = new ArrayList<>();
    }
    return matchReferenceTypes;
  }
  
  @Override
  public void setMatchReferenceTypes(List<String> matchReferenceTypes)
  {
    this.matchReferenceTypes = matchReferenceTypes;
  }
  
  @Override
  public IPropertyInfoModel getPropertyInfoModel()
  {
    return propertyInfoModel;
  }
  
  @JsonDeserialize(as = PropertyInfoModel.class)
  @Override
  public void setPropertyInfoModel(IPropertyInfoModel propertyInfoModel)
  {
    this.propertyInfoModel = propertyInfoModel;
  }
}
