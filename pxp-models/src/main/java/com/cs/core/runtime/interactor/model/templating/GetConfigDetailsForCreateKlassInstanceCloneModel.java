package com.cs.core.runtime.interactor.model.templating;

import java.util.ArrayList;
import java.util.List;

public class GetConfigDetailsForCreateKlassInstanceCloneModel extends AbstractGetConfigDetailsModel
    implements IGetConfigDetailsForCreateKlassInstanceCloneModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    referencedRelationshipIds;
  protected List<String>    sideIds;
  protected List<String>    mandatoryAttributeIds;
  protected List<String>    shouldAttributeIds;
  protected List<String>    mandatoryTagIds;
  protected List<String>    shouldTagIds;
  protected List<String>    identifierAttributeIds;
  protected Boolean         isDuplicationAllowed;
  
  @Override
  public List<String> getReferencedRelationshipIds()
  {
    if (referencedRelationshipIds == null) {
      referencedRelationshipIds = new ArrayList<>();
    }
    return referencedRelationshipIds;
  }
  
  @Override
  public void setReferencedRelationshipIds(List<String> referencedRelationshipIds)
  {
    this.referencedRelationshipIds = referencedRelationshipIds;
  }
  
  @Override
  public List<String> getSideIds()
  {
    if (sideIds == null) {
      sideIds = new ArrayList<>();
    }
    return sideIds;
  }
  
  @Override
  public void setSideIds(List<String> sideIds)
  {
    this.sideIds = sideIds;
  }

  @Override
  public List<String> getMandatoryAttributeIds()
  {
    if (mandatoryAttributeIds == null) {
      mandatoryAttributeIds = new ArrayList<>();
    }
    return mandatoryAttributeIds;
  }
  
  @Override
  public void setMandatoryAttributeIds(List<String> mandatoryAttributeIds)
  {
    this.mandatoryAttributeIds = mandatoryAttributeIds;
  }
  
  @Override
  public List<String> getMandatoryTagIds()
  {
    if (mandatoryTagIds == null) {
      mandatoryTagIds = new ArrayList<>();
    }
    return mandatoryTagIds;
  }
  
  @Override
  public void setMandatoryTagIds(List<String> mandatoryTagIds)
  {
    this.mandatoryTagIds = mandatoryTagIds;
  }
  
  @Override
  public List<String> getShouldAttributeIds()
  {
    if (shouldAttributeIds == null) {
      shouldAttributeIds = new ArrayList<>();
    }
    return shouldAttributeIds;
  }
  
  @Override
  public void setShouldAttributeIds(List<String> shouldAttributeIds)
  {
    this.shouldAttributeIds = shouldAttributeIds;
  }
  
  @Override
  public List<String> getShouldTagIds()
  {
    if (shouldTagIds == null) {
      shouldTagIds = new ArrayList<>();
    }
    return shouldTagIds;
  }
  
  @Override
  public void setShouldTagIds(List<String> shouldTagIds)
  {
    this.shouldTagIds = shouldTagIds;
  }
  
  @Override
  public List<String> getIdentifierAttributeIds()
  {
    if (identifierAttributeIds == null) {
      identifierAttributeIds = new ArrayList<>();
    }
    return identifierAttributeIds;
  }
  
  @Override
  public void setIdentifierAttributeIds(List<String> identifierAttributeIds)
  {
    this.identifierAttributeIds = identifierAttributeIds;
  }
  
  @Override
  public Boolean getIsDuplicationAllowed()
  {
    return isDuplicationAllowed;
  }
  
  @Override
  public void setIsDuplicationAllowed(Boolean isDuplicationAllowed)
  {
    this.isDuplicationAllowed = isDuplicationAllowed;
  }
  
}
