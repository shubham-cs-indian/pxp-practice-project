package com.cs.core.config.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.language.IGetLanguagesRequestModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesResponseModel;
import com.cs.core.config.strategy.usecase.language.IGetLanguagesStrategy;

@Service
public class GetLanguagesService<P extends IGetLanguagesRequestModel, R extends IGetLanguagesResponseModel>
    extends AbstractGetConfigService<IGetLanguagesRequestModel, IGetLanguagesResponseModel> implements IGetLanguagesService {
  
  @Autowired
  protected IGetLanguagesStrategy getLanguagesStrategy;
  
  @Override
  protected IGetLanguagesResponseModel executeInternal(IGetLanguagesRequestModel getLanguagesRequestModel) throws Exception
  {
    return getLanguagesStrategy.execute(getLanguagesRequestModel);
  }
}
