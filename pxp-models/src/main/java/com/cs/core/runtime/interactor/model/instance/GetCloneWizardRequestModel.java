package com.cs.core.runtime.interactor.model.instance;


import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.configdetails.IGetCloneWizardRequestModel;

public class GetCloneWizardRequestModel implements IGetCloneWizardRequestModel {
  
  private static final long serialVersionUID = 1L;
  public String             id;
  public List<String>       selectedTypesIds;
  public List<String>       selectedTaxonomyIds;
  public Boolean            isForLinkedVariant = false;
  public String             parentNatureKlassId;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
    
  }
  
  @Override
  public List<String> getSelectedTypesIds()
  {
    if(selectedTypesIds == null) {
      selectedTypesIds = new ArrayList<>();
    }
    return selectedTypesIds;
  }
  
  @Override
  public void setSelectedTypesIds(List<String> selectedTypesIds)
  {
    
    this.selectedTypesIds = selectedTypesIds;
    
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    if(selectedTaxonomyIds == null) {
      selectedTaxonomyIds = new ArrayList<>();
    }
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    
    this.selectedTaxonomyIds = selectedTaxonomyIds;
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
