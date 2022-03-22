package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingResponseModel;
import com.cs.di.config.mappings.IBulkSaveMappingService;

@Service
public class BulkSaveMapping
    extends AbstractSaveConfigInteractor<IBulkSaveMappingModel, IBulkSaveMappingResponseModel>
    implements IBulkSaveMapping {

  @Autowired protected IBulkSaveMappingService bulkSaveMappingService;

  @Override public IBulkSaveMappingResponseModel executeInternal(IBulkSaveMappingModel saveProfileModel)
      throws Exception
  {
    return bulkSaveMappingService.execute(saveProfileModel);
  }

}
