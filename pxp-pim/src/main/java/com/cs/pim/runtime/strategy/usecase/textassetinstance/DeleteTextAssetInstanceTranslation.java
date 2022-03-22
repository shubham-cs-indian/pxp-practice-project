package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.textassetinstance.IDeleteTextAssetInstanceTranslation;
import com.cs.pim.runtime.textassetinstance.IDeleteTextAssetInstanceTranslationService;

@Service
public class DeleteTextAssetInstanceTranslation
    extends AbstractRuntimeInteractor<IDeleteTranslationRequestModel, IDeleteTranslationResponseModel>
    implements IDeleteTextAssetInstanceTranslation {
  
  @Autowired
  protected IDeleteTextAssetInstanceTranslationService deleteTextAssetInstanceTranslationService;
  
  @Override
  public IDeleteTranslationResponseModel executeInternal(IDeleteTranslationRequestModel dataModel) throws Exception
  {
    return deleteTextAssetInstanceTranslationService.execute(dataModel);
  }
}
