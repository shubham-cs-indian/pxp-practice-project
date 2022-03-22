package com.cs.core.runtime.interactor.model.offboarding;

import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CustomExportRequestModel extends IdsListParameterModel
    implements ICustomExportRequestModel {
  
  private static final long                    serialVersionUID = 1L;
  protected String                             module;
  protected String                             collectionId;
  protected String                             bookmarkId;
  protected IGetKlassInstanceTreeStrategyModel filterResultsToSave;
  
  public String getBookmarkId()
  {
    return bookmarkId;
  }
  
  public void setBookmarkId(String bookmarkId)
  {
    this.bookmarkId = bookmarkId;
  }
  
  public String getModule()
  {
    return module;
  }
  
  public void setModule(String module)
  {
    this.module = module;
  }
  
  public String getCollectionId()
  {
    return collectionId;
  }
  
  public void setCollectionId(String collectionId)
  {
    this.collectionId = collectionId;
  }
  
  public IGetKlassInstanceTreeStrategyModel getFilterResultsToSave()
  {
    return filterResultsToSave;
  }
  
  @JsonDeserialize(as = GetKlassInstanceTreeStrategyModel.class)
  public void setFilterResultsToSave(IGetKlassInstanceTreeStrategyModel filterResultsToSave)
  {
    this.filterResultsToSave = filterResultsToSave;
  }
}
