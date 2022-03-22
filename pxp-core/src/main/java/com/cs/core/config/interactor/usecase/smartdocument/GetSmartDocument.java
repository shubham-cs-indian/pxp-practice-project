package com.cs.core.config.interactor.usecase.smartdocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.smartdocument.IGetSmartDocumentService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetSmartDocument extends AbstractGetConfigInteractor<IIdParameterModel, IGetSmartDocumentWithTemplateModel>
    implements IGetSmartDocument {
  
  @Autowired
  protected IGetSmartDocumentService getSmartDocumentService;
  
  @Override
  public IGetSmartDocumentWithTemplateModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getSmartDocumentService.execute(dataModel);
  }
}
