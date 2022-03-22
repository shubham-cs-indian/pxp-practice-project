package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkDeleteInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IDeleteInstanceVariantModel;
import com.cs.core.runtime.interactor.model.instance.ISaveStrategyInstanceResponseModel;
import com.cs.core.runtime.interactor.model.instance.SaveStrategyInstanceResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.BulkDeleteInstanceVariantsResponseModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DeleteInstanceVariantModel implements IDeleteInstanceVariantModel {
  
  protected IBulkDeleteInstanceVariantsResponseModel bulkDeleteReturnModel;
  
  protected ISaveStrategyInstanceResponseModel       saveStrategyInstanceResponseModel;
  
  @Override
  public IBulkDeleteInstanceVariantsResponseModel getBulkDeleteReturnModel()
  {
    return bulkDeleteReturnModel;
  }
  
  @JsonDeserialize(as = BulkDeleteInstanceVariantsResponseModel.class)
  @Override
  public void setBulkDeleteReturnModel(
      IBulkDeleteInstanceVariantsResponseModel bulkDeleteReturnModel)
  {
    this.bulkDeleteReturnModel = bulkDeleteReturnModel;
  }
  
  @Override
  public ISaveStrategyInstanceResponseModel getSaveStrategyInstanceResponseModel()
  {
    return saveStrategyInstanceResponseModel;
  }
  
  @JsonDeserialize(as = SaveStrategyInstanceResponseModel.class)
  @Override
  public void setSaveStrategyInstanceResponseModel(
      ISaveStrategyInstanceResponseModel saveStrategyInstanceResponseModel)
  {
    this.saveStrategyInstanceResponseModel = saveStrategyInstanceResponseModel;
  }
}
