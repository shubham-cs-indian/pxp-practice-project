package com.cs.core.runtime.interactor.model.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeStrategyModel;

import java.util.ArrayList;
import java.util.List;

public class GetTaxonomyTreeModel extends GetKlassInstanceTreeStrategyModel
    implements IGetTaxonomyTreeModel {
  
  private static final long                         serialVersionUID = 1L;
  protected String                                  searchText;
  protected String                                  clickedTaxonomyId;
  protected List<String>                            leafIds          = new ArrayList<>();
  protected Boolean                                 isKlassTaxonomy;
  protected List<IConfigEntityTreeInformationModel> categoryInfo     = new ArrayList<>();
  protected String                                  collectionId;
  
  @Override
  public String getSearchText()
  {
    return searchText;
  }
  
  @Override
  public void setSearchText(String searchText)
  {
    this.searchText = searchText;
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
  public List<String> getLeafIds()
  {
    return leafIds;
  }
  
  @Override
  public void setLeafIds(List<String> leafIds)
  {
    this.leafIds = leafIds;
  }
  
  @Override
  public Boolean getIsKlassTaxonomy()
  {
    return isKlassTaxonomy;
  }
  
  @Override
  public void setIsKlassTaxonomy(Boolean isKlassTaxonomy)
  {
    this.isKlassTaxonomy = isKlassTaxonomy;
  }
  
  @Override
  public List<IConfigEntityTreeInformationModel> getCategoryInfo()
  {
    return categoryInfo;
  }
  
  @Override
  public void setCategoryInfo(List<IConfigEntityTreeInformationModel> categoryInfo)
  {
    this.categoryInfo = categoryInfo;
  }
  
  @Override
  public String getCollectionId()
  {
    return collectionId;
  }
  
  @Override
  public void setCollectionId(String collectionId)
  {
    this.collectionId = collectionId;
  }
}
