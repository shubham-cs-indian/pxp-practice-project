package com.cs.core.config.interactor.usecase.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.language.IGetAllLanguageCodesService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Component
public class GetAllLanguageCodes extends AbstractGetConfigInteractor<IModel, IIdsListParameterModel>
    implements IGetAllLanguageCodes {
  
  @Autowired
  protected IGetAllLanguageCodesService getAllLanguageCodesService;
  
  @Override
  public IIdsListParameterModel executeInternal(IModel dataModel) throws Exception
  {
    return getAllLanguageCodesService.execute(dataModel);
  }
}
