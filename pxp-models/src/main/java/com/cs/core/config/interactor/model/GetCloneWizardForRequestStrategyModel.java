package com.cs.core.config.interactor.model;

import java.util.List;

public class GetCloneWizardForRequestStrategyModel
    implements IGetCloneWizardForRequestStrategyModel {
  
  private static final long serialVersionUID   = 1L;
  public List<String>       typesIdsToClone;
  public List<String>       taxonomyIdsToClone;
  public List<String>       typeIds;
  public List<String>       taxonomyIds;
  public Boolean            isForLinkedVariant = false;
  public String             parentNatureKlassId;
  
  @Override
  public List<String> getTypesIdsToClone()
  {
    return typesIdsToClone;
  }
  
  @Override
  public void setTypesIdsToClone(List<String> typesIdsToClone)
  {
    this.typesIdsToClone = typesIdsToClone;
  }
  
  @Override
  public List<String> getTaxonomyIdsToClone()
  {
    return taxonomyIdsToClone;
  }
  
  @Override
  public void setTaxonomyIdsToClone(List<String> taxonomyIdsToClone)
  {
    this.taxonomyIdsToClone = taxonomyIdsToClone;
  }
  
  @Override
  public List<String> getTypesIds()
  {
    return typeIds;
  }
  
  @Override
  public void setTypesIds(List<String> typeIds)
  {
    this.typeIds = typeIds;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public Boolean getIsForLinkedVariant()
  {
    return isForLinkedVariant;
  }
  
  @Override
  public void setIsForLinkedVariant(Boolean isForLinkedVariant)
  {
    this.isForLinkedVariant = isForLinkedVariant;
  }
  
  @Override
  public String getParentNatureKlassId()
  {
    return parentNatureKlassId;
  }
  
  @Override
  public void setParentNatureKlassId(String parentNatureKlassId)
  {
    this.parentNatureKlassId = parentNatureKlassId;
  }
}
