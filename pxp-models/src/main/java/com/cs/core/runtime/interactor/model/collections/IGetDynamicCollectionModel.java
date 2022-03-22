package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTreeInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;

import java.util.List;

public interface IGetDynamicCollectionModel extends IDynamicCollectionModel {
  
  public static final String FROM            = "from";
  public static final String TOTAL_CONTENTS  = "totalContents";
  public static final String TOTAL_CHILDREN  = "totalChildren";
  public static final String TAXONOMIES      = "taxonomies";
  public static final String FILTER_INFO     = "filterInfo";
  public static final String KLASS_INSTANCES = "klassInstances";
  public static final String CHILDREN        = "children";
  public static final String TAXONOMY_TREE   = "taxonomyTree";
  
  public IIdLabelTreeInformationModel getTaxonomyTree();
  
  public void setTaxonomyTree(IIdLabelTreeInformationModel taxonomyTree);
  
  public List<ITaxonomyInformationModel> getTaxonomies();
  
  public void setTaxonomies(List<ITaxonomyInformationModel> taxonomies);
  
  public List<IKlassInstanceInformationModel> getKlassInstances();
  
  public void setKlassInstances(List<IKlassInstanceInformationModel> klassInstances);
  
  public List<ICollectionModel> getChildren();
  
  public void setChildren(List<ICollectionModel> children);
  
  public IGetFilterInfoModel getFilterInfo();
  
  public void setFilterInfo(IGetFilterInfoModel filterInfo);
  
  public int getFrom();
  
  public void setFrom(int from);
  
  public long getTotalContents();
  
  public void setTotalContents(long totalContents);
  
  public long getTotalChildren();
  
  public void setTotalChildren(long totalChildren);
}
