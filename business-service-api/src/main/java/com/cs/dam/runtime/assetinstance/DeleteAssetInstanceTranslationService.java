package com.cs.dam.runtime.assetinstance;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.translations.AbstractDeleteTranslationInstanceService;

@Service
public class DeleteAssetInstanceTranslationService
    extends AbstractDeleteTranslationInstanceService<IDeleteTranslationRequestModel, IDeleteTranslationResponseModel>
    implements IDeleteAssetInstanceTranslationService {
  
  @Override
  protected IDeleteTranslationResponseModel executeInternal(IDeleteTranslationRequestModel model) throws Exception
  {
    return super.executeInternal(model);
  }
  
}
