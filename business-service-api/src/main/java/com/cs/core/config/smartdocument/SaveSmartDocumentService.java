package com.cs.core.config.smartdocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.interactor.model.smartdocument.ISmartDocumentModel;
import com.cs.core.config.strategy.usecase.smartdocument.ISaveSmartDocumentStrategy;

@Service
public class SaveSmartDocumentService extends AbstractSaveConfigService<ISmartDocumentModel, IGetSmartDocumentWithTemplateModel>
    implements ISaveSmartDocumentService {
  
  @Autowired
  protected ISaveSmartDocumentStrategy saveSmartDocumentStrategy;
  
  @Override
  public IGetSmartDocumentWithTemplateModel executeInternal(ISmartDocumentModel dataModel) throws Exception
  {
    return saveSmartDocumentStrategy.execute(dataModel);
  }
}
