package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import java.util.List;
import java.util.Map;

public interface IReferencedNatureRelationshipInheritanceModel
    extends IGetReferencedNatureRelationshipModel {
  
  String SIDE1_KLASS_IDS                           = "side1KlassIds";
  String SIDE2_KLASS_IDS                           = "side2KlassIds";
  String PROPAGABLE_RELATIONSHIP_IDS_COUPLING_TYPE = "propagableRelationshipIdsCouplingType";
  
  public List<String> getSide1KlassIds();
  
  public void setSide1KlassIds(List<String> side1KlassIds);
  
  public List<String> getSide2KlassIds();
  
  public void setSide2KlassIds(List<String> side2KlassIds);
  
  public Map<String, IIdAndCouplingTypeModel> getPropagableRelationshipIdsCouplingType();
  
  public void setPropagableRelationshipIdsCouplingType(
      Map<String, IIdAndCouplingTypeModel> propagableRelationshipIdsCouplingType);
}
