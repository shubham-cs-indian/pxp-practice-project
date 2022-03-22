package com.cs.core.config.interactor.usecase.smartdocument.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.config.smartdocument.template.IGetAllSmartDocumentTemplateService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllSmartDocumentTemplate extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<ISmartDocumentTemplateModel>>
    implements IGetAllSmartDocumentTemplate {
  
  @Autowired
  protected IGetAllSmartDocumentTemplateService getAllSmartDocumentTemplateService;
  
  @Override
  public IListModel<ISmartDocumentTemplateModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAllSmartDocumentTemplateService.execute(dataModel);
  }
}
