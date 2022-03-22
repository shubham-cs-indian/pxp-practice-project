package com.cs.core.runtime.interactor.model.offboarding;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;

public interface ICustomExportRequestModel extends IIdsListParameterModel {
  
  public static final String MODULE                 = "module";
  public static final String FILTER_RESULTS_TO_SAVE = "filterResultsToSave";
  public static final String COLLECTION_ID          = "collectionId";
  
  public String getModule();
  
  public void setModule(String module);
  
  public IGetKlassInstanceTreeStrategyModel getFilterResultsToSave();
  
  public void setFilterResultsToSave(IGetKlassInstanceTreeStrategyModel filterResultsToSave);
  
  public String getCollectionId();
  
  public void setCollectionId(String collectionId);
  
  public String getBookmarkId();
  
  public void setBookmarkId(String bookmarkId);
}
