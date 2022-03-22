package com.cs.pim.runtime.supplierinstance;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.translations.AbstractDeleteTranslationInstanceService;

@Service
public class DeleteSupplierInstanceTranslationService
    extends AbstractDeleteTranslationInstanceService<IDeleteTranslationRequestModel, IDeleteTranslationResponseModel>
    implements IDeleteSupplierInstanceTranslationService {
  
  @Override
  public IDeleteTranslationResponseModel execute(IDeleteTranslationRequestModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
}
