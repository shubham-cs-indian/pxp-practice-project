package com.cs.core.runtime.interactor.model.templating;

import java.util.List;

public interface IGetConfigDetailsForCreateKlassInstanceCloneModel extends IGetConfigDetailsModel {
  
  String REFERENCED_RELATIONSHIPS_IDS = "referencedRelationshipIds";
  String SIDE_IDS                     = "sideIds";
  String MANDATORY_ATTRIBUTE_IDS      = "mandatoryAttributeIds";
  String MANDATORY_TAG_IDS            = "mandatoryTagIds";
  String SHOULD_ATTRIBUTE_IDS         = "shouldAttributeIds";
  String SHOULD_TAG_IDS               = "shouldTagIds";
  String IDENTIFIER_ATTRIBUTE_IDS     = "identifierAttributeIds";
  String IS_DUPLICATION_ALLOWED       = "isDuplicationAllowed";
  
  public List<String> getReferencedRelationshipIds();
  
  public void setReferencedRelationshipIds(List<String> referencedRelationshipIds);
  
  public List<String> getSideIds();
  
  public void setSideIds(List<String> sideIds);
  
  public List<String> getMandatoryAttributeIds();
  
  public void setMandatoryAttributeIds(List<String> mandatoryAttributeIds);
  
  public List<String> getMandatoryTagIds();
  
  public void setMandatoryTagIds(List<String> mandatoryTagIds);
  
  public List<String> getShouldAttributeIds();
  
  public void setShouldAttributeIds(List<String> shouldAttributeIds);
  
  public List<String> getShouldTagIds();
  
  public void setShouldTagIds(List<String> shouldTagIds);
  
  public List<String> getIdentifierAttributeIds();
  
  public void setIdentifierAttributeIds(List<String> identifierAttributeIds);
  
  public Boolean getIsDuplicationAllowed();
  public void setIsDuplicationAllowed(Boolean isDuplicationAllowed);
}
