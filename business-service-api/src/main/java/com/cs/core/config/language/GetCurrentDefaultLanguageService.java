package com.cs.core.config.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.strategy.usecase.language.IGetCurrrentDefaultLanguageStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Service
public class GetCurrentDefaultLanguageService extends AbstractGetConfigService<IModel, IConfigEntityInformationModel>
    implements IGetCurrentDefaultLanguageService {
  
  @Autowired
  private IGetCurrrentDefaultLanguageStrategy getCurrentDefaultLanguageStrategy;
  
  @Override
  public IConfigEntityInformationModel executeInternal(IModel dataModel) throws Exception
  {
    return getCurrentDefaultLanguageStrategy.execute(dataModel);
  }
}
