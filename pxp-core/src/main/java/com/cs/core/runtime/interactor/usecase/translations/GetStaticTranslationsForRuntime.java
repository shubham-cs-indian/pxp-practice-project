package com.cs.core.runtime.interactor.usecase.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IGetStaticLabelTranslationsResponseModel;
import com.cs.core.runtime.interactor.model.translations.IGetStaticTranslationsForRuntimeRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.strategy.usecase.translations.IGetStaticTranslationsForRuntimeStrategy;

@Service
public class GetStaticTranslationsForRuntime extends
    AbstractRuntimeInteractor<IGetStaticTranslationsForRuntimeRequestModel, IGetStaticLabelTranslationsResponseModel>
    implements IGetStaticTranslationsForRuntime {
  
  @Autowired
  protected IGetStaticTranslationsForRuntimeStrategy getStaticTranslationsForRuntimeStrategy;
  
  @Override
  public IGetStaticLabelTranslationsResponseModel executeInternal(
      IGetStaticTranslationsForRuntimeRequestModel dataModel) throws Exception
  {
    return getStaticTranslationsForRuntimeStrategy.execute(dataModel);
  }
}
