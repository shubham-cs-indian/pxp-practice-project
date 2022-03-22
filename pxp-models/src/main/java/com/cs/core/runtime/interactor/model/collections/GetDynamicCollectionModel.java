package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTreeInformationModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelTreeInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetDynamicCollectionModel extends DynamicCollectionModel
    implements IGetDynamicCollectionModel {
  
  private static final long                      serialVersionUID = 1L;
  protected List<ITaxonomyInformationModel>      taxonomies       = new ArrayList<>();
  protected IGetFilterInfoModel                  filterInfo;
  protected List<ICollectionModel>               children         = new ArrayList<>();
  protected List<IKlassInstanceInformationModel> klassInstances   = new ArrayList<>();
  protected int                                  from             = 0;
  protected long                                 totalContents    = 0l;
  protected long                                 totalChildren    = 0l;
  protected IIdLabelTreeInformationModel         taxonomyTree;
  
  @Override
  public IIdLabelTreeInformationModel getTaxonomyTree()
  {
    return taxonomyTree;
  }
  
  @JsonDeserialize(as = IdLabelTreeInformationModel.class)
  @Override
  public void setTaxonomyTree(IIdLabelTreeInformationModel taxonomyTree)
  {
    this.taxonomyTree = taxonomyTree;
  }
  
  @Override
  public List<ITaxonomyInformationModel> getTaxonomies()
  {
    return taxonomies;
  }
  
  @Override
  public void setTaxonomies(List<ITaxonomyInformationModel> taxonomies)
  {
    this.taxonomies = taxonomies;
  }
  
  @Override
  public IGetFilterInfoModel getFilterInfo()
  {
    return filterInfo;
  }
  
  @Override
  public void setFilterInfo(IGetFilterInfoModel filterInfo)
  {
    this.filterInfo = filterInfo;
  }
  
  @Override
  public List<ICollectionModel> getChildren()
  {
    return children;
  }
  
  @JsonDeserialize(contentAs = CollectionModel.class)
  @Override
  public void setChildren(List<ICollectionModel> children)
  {
    this.children = children;
  }
  
  @Override
  public List<IKlassInstanceInformationModel> getKlassInstances()
  {
    return klassInstances;
  }
  
  @Override
  public void setKlassInstances(List<IKlassInstanceInformationModel> klassInstances)
  {
    this.klassInstances = klassInstances;
  }
  
  @Override
  public int getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(int from)
  {
    this.from = from;
  }
  
  @Override
  public long getTotalContents()
  {
    return totalContents;
  }
  
  @Override
  public void setTotalContents(long totalKlassInstances)
  {
    this.totalContents = totalKlassInstances;
  }
  
  @Override
  public long getTotalChildren()
  {
    return totalChildren;
  }
  
  @Override
  public void setTotalChildren(long totalChildren)
  {
    this.totalChildren = totalChildren;
  }
}
