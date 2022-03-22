package com.cs.core.config.smartdocument.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.config.strategy.usecase.smartdocument.template.ISaveSmartDocumentTemplateStrategy;

@Service
public class SaveSmartDocumentTemplateService
    extends AbstractSaveConfigService<ISmartDocumentTemplateModel, IGetSmartDocumentTemplateWithPresetModel>
    implements ISaveSmartDocumentTemplateService {
  
  @Autowired
  protected ISaveSmartDocumentTemplateStrategy saveSmartDocumentTemplateStrategy;
  
  @Override
  public IGetSmartDocumentTemplateWithPresetModel executeInternal(ISmartDocumentTemplateModel dataModel) throws Exception
  {
    return saveSmartDocumentTemplateStrategy.execute(dataModel);
  }
}
