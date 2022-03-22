package com.cs.core.config.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.strategy.usecase.language.IGetLanguageStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetLanguageService extends AbstractGetConfigService<IIdParameterModel, IGetLanguageModel> implements IGetLanguageService {
  
  @Autowired
  protected IGetLanguageStrategy getLanguageStrategy;
  
  @Override
  public IGetLanguageModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getLanguageStrategy.execute(dataModel);
  }
}
