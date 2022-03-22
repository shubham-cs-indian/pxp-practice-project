package com.cs.core.config.interactor.usecase.smartdocument.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.template.IGetSmartDocumentTemplateWithPresetModel;
import com.cs.core.config.smartdocument.template.IGetSmartDocumentTemplateService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetSmartDocumentTemplate extends AbstractGetConfigInteractor<IIdParameterModel, IGetSmartDocumentTemplateWithPresetModel>
    implements IGetSmartDocumentTemplate {
  
  @Autowired
  protected IGetSmartDocumentTemplateService getSmartDocumentTemplateService;
  
  @Override
  public IGetSmartDocumentTemplateWithPresetModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getSmartDocumentTemplateService.execute(dataModel);
  }
}
