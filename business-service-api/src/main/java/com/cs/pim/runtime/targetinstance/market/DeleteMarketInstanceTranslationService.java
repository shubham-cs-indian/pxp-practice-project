package com.cs.pim.runtime.targetinstance.market;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.translations.AbstractDeleteTranslationInstanceService;

@Service
public class DeleteMarketInstanceTranslationService
    extends AbstractDeleteTranslationInstanceService<IDeleteTranslationRequestModel, IDeleteTranslationResponseModel>
    implements IDeleteMarketInstanceTranslationService {
  
  @Override
  public IDeleteTranslationResponseModel execute(IDeleteTranslationRequestModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
}
