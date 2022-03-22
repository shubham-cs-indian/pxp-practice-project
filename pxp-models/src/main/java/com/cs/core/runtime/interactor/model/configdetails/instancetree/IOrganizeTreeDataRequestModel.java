package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.instancetree.INewInstanceTreeRequestModel;


public interface IOrganizeTreeDataRequestModel extends INewInstanceTreeRequestModel {
  
  public static final String KLASS_TAXONOMY_INFO = "klassTaxonomyInfo";
  public static final String CLICKED_TAXONOMY_ID = "clickedTaxonomyId";
  public static final String PARENT_TAXONOMY_ID  = "parentTaxonomyId";
  
  public List<ICategoryInformationModel> getKlassTaxonomyInfo();
  public void setKlassTaxonomyInfo(List<ICategoryInformationModel> taxonomyInfo);
  
  public String getClickedTaxonomyId();
  public void setClickedTaxonomyId(String clickedTaxonomyId);
  
  public String getParentTaxonomyId();
  public void setParentTaxonomyId(String parentTaxonomyId);
}
