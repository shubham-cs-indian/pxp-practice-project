package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;

import java.util.ArrayList;
import java.util.List;

public class ConfigDetailsForBulkPropagationRequestModel extends MulticlassificationRequestModel
    implements IConfigDetailsForBulkPropagationRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    addedKlassIds;
  protected List<String>    addedTaxonomyIds;
  
  @Override
  public List<String> getAddedKlassIds()
  {
    if (addedKlassIds == null) {
      addedKlassIds = new ArrayList<>();
    }
    return addedKlassIds;
  }
  
  @Override
  public void setAddedKlassIds(List<String> addedKlassIds)
  {
    this.addedKlassIds = addedKlassIds;
  }
  
  @Override
  public List<String> getAddedTaxonomyIds()
  {
    if (addedTaxonomyIds == null) {
      addedTaxonomyIds = new ArrayList<>();
    }
    return addedTaxonomyIds;
  }
  
  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    this.addedTaxonomyIds = addedTaxonomyIds;
  }
}
