package com.cs.core.config.language;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.strategy.usecase.language.IGetLanguageByLocaleIdStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetLanguageByLocaleIdService extends AbstractGetConfigService<IIdParameterModel, IGetLanguageModel>
    implements IGetLanguageByLocaleIdService {

  @Autowired
  protected IGetLanguageByLocaleIdStrategy getLanguageByLocaleIdStrategy;

  @Override public IGetLanguageModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getLanguageByLocaleIdStrategy.execute(dataModel);
  }
}
