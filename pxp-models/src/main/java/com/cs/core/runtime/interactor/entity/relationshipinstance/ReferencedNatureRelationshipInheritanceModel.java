package com.cs.core.runtime.interactor.entity.relationshipinstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.relationship.GetReferencedNatureRelationshipModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ReferencedNatureRelationshipInheritanceModel extends GetReferencedNatureRelationshipModel
    implements IReferencedNatureRelationshipInheritanceModel {
  
  private static final long                      serialVersionUID = 1L;
  protected List<String>                         side1KlassIds;
  protected List<String>                         side2KlassIds;
  protected Map<String, IIdAndCouplingTypeModel> propagableRelationshipIdsCouplingType;
  
  @Override
  public List<String> getSide1KlassIds()
  {
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
    return side2KlassIds;
  }
  
  @Override
  public void setSide2KlassIds(List<String> side2KlassIds)
  {
    this.side2KlassIds = side2KlassIds;
  }
  
  @Override
  public Map<String, IIdAndCouplingTypeModel> getPropagableRelationshipIdsCouplingType()
  {
    if (propagableRelationshipIdsCouplingType == null) {
      propagableRelationshipIdsCouplingType = new HashMap<>();
    }
    return propagableRelationshipIdsCouplingType;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndCouplingTypeModel.class)
  public void setPropagableRelationshipIdsCouplingType(Map<String, IIdAndCouplingTypeModel> propagableRelationshipIdsCouplingType)
  {
    this.propagableRelationshipIdsCouplingType = propagableRelationshipIdsCouplingType;
  }
}
