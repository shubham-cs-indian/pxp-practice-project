package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.targetinstance.market.IDeleteMarketInstanceTranslationService;

@Service
public class DeleteMarketInstanceTranslation
    extends AbstractRuntimeInteractor<IDeleteTranslationRequestModel, IDeleteTranslationResponseModel>
    implements IDeleteMarketInstanceTranslation {
  
  @Autowired
  protected IDeleteMarketInstanceTranslationService deleteMarketInstanceTranslationService;
  
  @Override
  public IDeleteTranslationResponseModel executeInternal(IDeleteTranslationRequestModel dataModel) throws Exception
  {
    return deleteMarketInstanceTranslationService.execute(dataModel);
  }
}
