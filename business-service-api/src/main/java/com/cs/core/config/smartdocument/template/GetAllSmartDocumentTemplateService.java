package com.cs.core.config.smartdocument.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.template.ISmartDocumentTemplateModel;
import com.cs.core.config.strategy.usecase.smartdocument.template.IGetAllSmartDocumentTemplateStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllSmartDocumentTemplateService extends AbstractGetConfigService<IIdParameterModel, IListModel<ISmartDocumentTemplateModel>>
    implements IGetAllSmartDocumentTemplateService {
  
  @Autowired
  protected IGetAllSmartDocumentTemplateStrategy getAllSmartDocumentTemplateStrategy;
  
  @Override
  public IListModel<ISmartDocumentTemplateModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAllSmartDocumentTemplateStrategy.execute(dataModel);
  }
}
