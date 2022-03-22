package com.cs.core.config.interactor.usecase.smartdocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.interactor.model.smartdocument.ISmartDocumentModel;
import com.cs.core.config.smartdocument.ISaveSmartDocumentService;

@Service
public class SaveSmartDocument extends AbstractSaveConfigInteractor<ISmartDocumentModel, IGetSmartDocumentWithTemplateModel>
    implements ISaveSmartDocument {
  
  @Autowired
  protected ISaveSmartDocumentService saveSmartDocumentService;
  
  @Override
  public IGetSmartDocumentWithTemplateModel executeInternal(ISmartDocumentModel dataModel) throws Exception
  {
    return saveSmartDocumentService.execute(dataModel);
  }
}
