package com.cs.di.config.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.mapping.ICreateOutBoundMappingResponseModel;
import com.cs.core.config.interactor.model.mapping.ISaveOutBoundMappingModel;
import com.cs.core.config.strategy.usecase.mapping.ISaveOutBoundMappingStrategy;

@Service
public class SaveOutBoundMappingService extends
    AbstractSaveConfigService<ISaveOutBoundMappingModel, ICreateOutBoundMappingResponseModel>
    implements ISaveOutBoundMappingService {
  
  @Autowired
  protected ISaveOutBoundMappingStrategy saveOutBoundMappingStrategy;
  
  @Override
  public ICreateOutBoundMappingResponseModel executeInternal(
      ISaveOutBoundMappingModel saveProfileModel) throws Exception
  {
    return saveOutBoundMappingStrategy.execute(saveProfileModel);
  }
}
