package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.mapping.ICreateOutBoundMappingResponseModel;
import com.cs.core.config.interactor.model.mapping.ISaveOutBoundMappingModel;
import com.cs.di.config.mappings.ISaveOutBoundMappingService;

@Service("saveOutBoundMapping")
public class SaveOutBoundMapping extends
    AbstractSaveConfigInteractor<ISaveOutBoundMappingModel, ICreateOutBoundMappingResponseModel>
    implements ISaveOutBoundMapping {
  
  @Autowired
  protected ISaveOutBoundMappingService saveOutBoundMappingService;
  
  @Override
  public ICreateOutBoundMappingResponseModel executeInternal(
      ISaveOutBoundMappingModel saveProfileModel) throws Exception
  {
    return saveOutBoundMappingService.execute(saveProfileModel);
  }
}
