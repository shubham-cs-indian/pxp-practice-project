package com.cs.di.config.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingResponseModel;
import com.cs.core.config.strategy.usecase.mapping.IBulkSaveMappingStrategy;

@Service
public class BulkSaveMappingService
    extends AbstractSaveConfigService<IBulkSaveMappingModel, IBulkSaveMappingResponseModel>
    implements IBulkSaveMappingService {

  @Autowired protected IBulkSaveMappingStrategy bulkSaveMappingStrategy;

  @Override public IBulkSaveMappingResponseModel executeInternal(IBulkSaveMappingModel saveProfileModel)
      throws Exception
  {
    return bulkSaveMappingStrategy.execute(saveProfileModel);
  }

}
