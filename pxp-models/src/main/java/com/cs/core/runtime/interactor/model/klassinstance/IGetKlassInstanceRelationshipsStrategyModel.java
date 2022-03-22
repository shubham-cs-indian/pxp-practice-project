package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;

import java.util.Map;
import java.util.Set;

public interface IGetKlassInstanceRelationshipsStrategyModel
    extends IGetKlassInstanceTreeStrategyModel {
  
  public static final String RELATIONSHIP_ID        = "relationshipId";
  public static final String BASE_TYPE              = "baseType";
  public static final String ENTITIES               = "entities";
  public static final String IS_NATURE_RELATIONSHIP = "isNatureRelationship";
  public static final String SIDE_ID                = "sideId";
  public static final String RFERENCED_ELEMENTS     = "referencedElements";
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public Boolean getIsNatureRelationship();
  
  public void setIsNatureRelationship(Boolean isNatureRelationship);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Set<String> getEntities();
  
  public void setEntities(Set<String> entities);
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
}
