package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CreateStaticCollectionModel extends StaticCollectionModel
    implements ICreateStaticCollectionModel {
  
  private static final long                    serialVersionUID = 1L;
  protected IGetKlassInstanceTreeStrategyModel filterResultsToSave;
  
  @Override
  public IGetKlassInstanceTreeStrategyModel getFilterResultsToSave()
  {
    return filterResultsToSave;
  }
  
  @JsonDeserialize(as = GetKlassInstanceTreeStrategyModel.class)
  @Override
  public void setFilterResultsToSave(IGetKlassInstanceTreeStrategyModel filterResultsToSave)
  {
    this.filterResultsToSave = filterResultsToSave;
  }
}
