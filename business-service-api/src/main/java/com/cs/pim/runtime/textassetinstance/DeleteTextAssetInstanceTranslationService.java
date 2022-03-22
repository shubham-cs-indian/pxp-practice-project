package com.cs.pim.runtime.textassetinstance;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.translations.AbstractDeleteTranslationInstanceService;

@Service
public class DeleteTextAssetInstanceTranslationService
    extends AbstractDeleteTranslationInstanceService<IDeleteTranslationRequestModel, IDeleteTranslationResponseModel>
    implements IDeleteTextAssetInstanceTranslationService {
  
  @Override
  public IDeleteTranslationResponseModel execute(IDeleteTranslationRequestModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
}
