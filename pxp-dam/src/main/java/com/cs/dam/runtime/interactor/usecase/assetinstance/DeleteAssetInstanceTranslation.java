package com.cs.dam.runtime.interactor.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IDeleteAssetInstanceTranslationService;

@Service
public class DeleteAssetInstanceTranslation extends
    AbstractRuntimeInteractor<IDeleteTranslationRequestModel, IDeleteTranslationResponseModel> implements IDeleteAssetInstanceTranslation {
  
  @Autowired
  protected IDeleteAssetInstanceTranslationService deleteAssetInstanceTranslationService;
  
  @Override
  protected IDeleteTranslationResponseModel executeInternal(IDeleteTranslationRequestModel model) throws Exception
  {
    return deleteAssetInstanceTranslationService.execute(model);
  }
}
