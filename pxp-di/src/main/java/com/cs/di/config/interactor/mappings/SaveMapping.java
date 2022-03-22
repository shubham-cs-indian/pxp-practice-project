package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.mapping.ICreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.ISaveMappingModel;
import com.cs.core.config.interactor.usecase.mapping.ISaveMapping;
import com.cs.di.config.mappings.ISaveMappingService;


@Service
public class SaveMapping
    extends AbstractSaveConfigInteractor<ISaveMappingModel, ICreateOrSaveMappingModel>
    implements ISaveMapping {
  
  @Autowired
  protected ISaveMappingService saveMappingService;
  
  @Override
  public ICreateOrSaveMappingModel executeInternal(ISaveMappingModel saveProfileModel) throws Exception
  {
    return saveMappingService.execute(saveProfileModel);
  }
}
