package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;

public class GetChildMajorTaxonomiesRequestModel extends ConfigGetAllRequestModel implements IGetChildMajorTaxonomiesRequestModel 
{
  
  private static final long serialVersionUID = 1L;
  protected String          taxonomyId;
  protected List<String>    taxonomyTypes;

  @Override
  public String getTaxonomyId()
  {
    return taxonomyId;
  }
  
  @Override
  public void setTaxonomyId(String taxonomyId)
  {
    this.taxonomyId = taxonomyId;
  }

  @Override
  public List<String> getTaxonomyTypes()
  {
    if (taxonomyTypes == null) {
      taxonomyTypes = new ArrayList<String>();
    }
    return taxonomyTypes;
  }

  @Override
  public void setTaxonomyTypes(List<String> taxonomyTypes)
  {
    this.taxonomyTypes = taxonomyTypes;
  } 
}