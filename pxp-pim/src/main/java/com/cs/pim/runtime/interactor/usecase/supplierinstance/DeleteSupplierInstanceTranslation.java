package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.supplierinstance.IDeleteSupplierInstanceTranslationService;

@Service
public class DeleteSupplierInstanceTranslation
    extends AbstractRuntimeInteractor<IDeleteTranslationRequestModel, IDeleteTranslationResponseModel>
    implements IDeleteSupplierInstanceTranslation {
  
  @Autowired
  protected IDeleteSupplierInstanceTranslationService deleteSupplierInstanceTranslationService;
  
  @Override
  public IDeleteTranslationResponseModel executeInternal(IDeleteTranslationRequestModel dataModel) throws Exception
  {
    return deleteSupplierInstanceTranslationService.execute(dataModel);
  }
}
