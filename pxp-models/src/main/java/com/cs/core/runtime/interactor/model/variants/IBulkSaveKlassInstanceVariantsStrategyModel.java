package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.instance.ISaveStrategyInstanceResponseModel;

import java.util.List;

public interface IBulkSaveKlassInstanceVariantsStrategyModel extends IBulkResponseModel {
  
  String INSTANCES_SAVE_RESPONSE = "instancesSaveResponse";
  
  public List<ISaveStrategyInstanceResponseModel> getInstancesSaveResponse();
  
  public void setInstancesSaveResponse(
      List<ISaveStrategyInstanceResponseModel> instancesSaveResponse);
  
  public void setSuccess(List<String> successIds);
}
