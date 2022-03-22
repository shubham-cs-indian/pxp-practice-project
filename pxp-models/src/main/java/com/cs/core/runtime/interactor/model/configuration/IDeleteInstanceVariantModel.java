package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkDeleteInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.model.instance.ISaveStrategyInstanceResponseModel;

public interface IDeleteInstanceVariantModel {
  
  public static String BULK_DELETE_RETURN_MODEL              = "bulkDeleteReturnModel";
  
  public static String SAVE_STRATEGY_INSTANCE_RESPONSE_MODEL = "saveStrategyInstanceResponseModel";
  
  public IBulkDeleteInstanceVariantsResponseModel getBulkDeleteReturnModel();
  
  public void setBulkDeleteReturnModel(
      IBulkDeleteInstanceVariantsResponseModel bulkDeleteReturnModel);
  
  public ISaveStrategyInstanceResponseModel getSaveStrategyInstanceResponseModel();
  
  public void setSaveStrategyInstanceResponseModel(
      ISaveStrategyInstanceResponseModel saveStrategyInstanceResponseModel);
}
