package com.cs.core.config.interactor.usecase.smartdocument.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.config.smartdocument.template.ISaveSmartDocumentTemplateService;

@Service
public class SaveSmartDocumentTemplate
    extends AbstractSaveConfigInteractor<ISmartDocumentTemplateModel, IGetSmartDocumentTemplateWithPresetModel>
    implements ISaveSmartDocumentTemplate {
  
  @Autowired
  protected ISaveSmartDocumentTemplateService saveSmartDocumentTemplateService;
  
  @Override
  public IGetSmartDocumentTemplateWithPresetModel executeInternal(ISmartDocumentTemplateModel dataModel) throws Exception
  {
    return saveSmartDocumentTemplateService.execute(dataModel);
  }
}
