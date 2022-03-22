package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;

public interface IGetKlassTaxonomyTreeRequestModel extends INewInstanceTreeParentRequestModel {
  
  public static final String KLASS_TAXONOMY_INFO = "klassTaxonomyInfo";
  public static final String CLICKED_TAXONOMY_ID = "clickedTaxonomyId";
  public static final String SEARCH_TEXT         = "searchText";
  public static final String SELECTED_CATEGORY   = "selectedCategory";
  
  
  public List<ICategoryInformationModel> getKlassTaxonomyInfo();
  public void setKlassTaxonomyInfo(List<ICategoryInformationModel> taxonomyInfo);
  
  public String getClickedTaxonomyId();
  public void setClickedTaxonomyId(String clickedTaxonomyId);
  
  public String getSearchText();
  public void setSearchText(String searchText);
  
  public String getSelectedCategory();
  public void setSelectedCategory(String selectedCategory);
  
}
