package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.entity.collections.IStaticCollection;
import com.cs.core.runtime.interactor.entity.configuration.base.ISortEntity;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IStaticCollectionModel extends IStaticCollection, IModel {
  
  public static final String KLASS_INSTANCE_IDS = "klassInstanceIds";
  public static final String SORT_OPTIONS       = "sortOptions";
  
  public List<String> getKlassInstanceIds();
  
  public void setKlassInstanceIds(List<String> klassInstanceIds);
  
  public List<ISortEntity> getSortOptions();
  
  public void setSortOptions(List<ISortEntity> sortOptions);
}
