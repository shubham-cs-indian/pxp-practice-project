package com.cs.core.config.interactor.usecase.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.language.IGetLanguagesRequestModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesResponseModel;
import com.cs.core.config.language.IGetLanguagesService;

@Service
public class GetLanguages<P extends IGetLanguagesRequestModel, R extends IGetLanguagesResponseModel>
    extends AbstractGetConfigInteractor<IGetLanguagesRequestModel, IGetLanguagesResponseModel>
    implements IGetLanguages {
  
  @Autowired
  protected IGetLanguagesService getLanguagesService;
  
  @Override
  protected IGetLanguagesResponseModel executeInternal(
      IGetLanguagesRequestModel getLanguagesRequestModel) throws Exception
  {
    return getLanguagesService.execute(getLanguagesRequestModel);
  }
}
