package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IRelationshipInstanceInformationModel extends IModel {
  
  public static final String ID                      = "id";
  public static final String RELATIONSHIP_MAPPING_ID = "relationshipMappingId";
  public static final String ELEMENTS                = "elements";
  
  public String getId();
  
  public void setId(String id);
  
  public String getRelationshipMappingId();
  
  public void setRelationshipMappingId(String relationshipMappingId);
  
  public List<? extends IKlassInstanceInformationModel> getElements();
  
  public void setElements(List<? extends IKlassInstanceInformationModel> elements);
}
