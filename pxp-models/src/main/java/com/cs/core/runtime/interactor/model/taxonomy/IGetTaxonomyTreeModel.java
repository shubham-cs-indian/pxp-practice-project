package com.cs.core.runtime.interactor.model.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;

import java.util.List;

public interface IGetTaxonomyTreeModel extends IGetKlassInstanceTreeStrategyModel {
  
  public static final String SEARCH_TEXT         = "searchText";
  public static final String CLICKED_TAXONOMY_ID = "clickedTaxonomyId";
  public static final String LEAF_IDS            = "leafIds";
  public static final String IS_KLASS_TAXONOMY   = "isKlassTaxonomy";
  public static final String CATEGORY_INFO       = "categoryInfo";
  public static final String COLLECTION_ID       = "collectionId";
  
  public String getSearchText();
  
  public void setSearchText(String searchText);
  
  public String getClickedTaxonomyId();
  
  public void setClickedTaxonomyId(String clickedTaxonomyId);
  
  public List<String> getLeafIds();
  
  public void setLeafIds(List<String> leafIds);
  
  public Boolean getIsKlassTaxonomy();
  
  public void setIsKlassTaxonomy(Boolean isKlassTaxonomy);
  
  public List<IConfigEntityTreeInformationModel> getCategoryInfo();
  
  public void setCategoryInfo(List<IConfigEntityTreeInformationModel> categoryInfo);
  
  public String getCollectionId();
  
  public void setCollectionId(String collectionId);
}
