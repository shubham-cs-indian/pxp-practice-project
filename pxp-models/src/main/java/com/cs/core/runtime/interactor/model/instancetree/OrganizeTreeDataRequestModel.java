package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.configdetails.instancetree.IOrganizeTreeDataRequestModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;


public class OrganizeTreeDataRequestModel extends GetInstanceTreeRequestModel implements IOrganizeTreeDataRequestModel {
  
  private static final long                    serialVersionUID = 1L;
  protected List<ICategoryInformationModel>    taxonomyInfo;
  protected String                             clickedTaxonomyId;
  protected String                             parentTaxonomyId;
  
  @Override
  public List<ICategoryInformationModel> getKlassTaxonomyInfo()
  {
    return taxonomyInfo;
  }
  
  @Override
  public void setKlassTaxonomyInfo(List<ICategoryInformationModel> taxonomyInfo)
  {
    this.taxonomyInfo = taxonomyInfo;
  }
  
  @Override
  public String getClickedTaxonomyId()
  {
    return clickedTaxonomyId;
  }
  
  @Override
  public void setClickedTaxonomyId(String clickedTaxonomyId)
  {
    this.clickedTaxonomyId = clickedTaxonomyId;
  }

  @Override
  public String getParentTaxonomyId()
  {
    return parentTaxonomyId;
  }

  @Override
  public void setParentTaxonomyId(String parentTaxonomyId)
  {
    this.parentTaxonomyId = parentTaxonomyId;
  }
  
}
