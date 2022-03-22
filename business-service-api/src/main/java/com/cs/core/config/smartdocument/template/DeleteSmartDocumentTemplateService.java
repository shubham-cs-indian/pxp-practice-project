package com.cs.core.config.smartdocument.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.config.strategy.usecase.smartdocument.template.IDeleteSmartDocumentTemplateStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class DeleteSmartDocumentTemplateService extends AbstractDeleteConfigService<IIdParameterModel, IBulkDeleteReturnModel>
    implements IDeleteSmartDocumentTemplateService {
  
  @Autowired
  protected IDeleteSmartDocumentTemplateStrategy deleteSmartDocumentTemplateStrategy;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return deleteSmartDocumentTemplateStrategy.execute(dataModel);
  }
}
