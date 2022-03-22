package com.cs.core.config.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.config.strategy.usecase.template.IDeleteTemplateStrategy;
import com.cs.core.config.template.IDeleteTemplateService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteTemplateService
    extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteTemplateService {
  
  @Autowired
  protected IDeleteTemplateStrategy deleteTemplateStrategy;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel templateModel) throws Exception
  {
    return deleteTemplateStrategy.execute(templateModel);
  }
}
