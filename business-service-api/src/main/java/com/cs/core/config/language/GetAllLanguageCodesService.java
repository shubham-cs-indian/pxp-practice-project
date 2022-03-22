package com.cs.core.config.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.strategy.usecase.language.IGetAllLanguageCodesStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Component
public class GetAllLanguageCodesService extends AbstractGetConfigService<IModel, IIdsListParameterModel>
    implements IGetAllLanguageCodesService {
  
  @Autowired
  protected IGetAllLanguageCodesStrategy getAllLanguageCodesStrategy;
  
  @Override
  public IIdsListParameterModel executeInternal(IModel dataModel) throws Exception
  {
    return getAllLanguageCodesStrategy.execute(dataModel);
  }
}
