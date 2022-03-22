package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;

public interface ICreateStaticCollectionModel extends IStaticCollectionModel {
  
  public static final String FILTER_RESULTS_TO_SAVE = "filterResultsToSave";
  
  public IGetKlassInstanceTreeStrategyModel getFilterResultsToSave();
  
  public void setFilterResultsToSave(IGetKlassInstanceTreeStrategyModel filterResultsToSave);
}
