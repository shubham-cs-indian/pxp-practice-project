package com.cs.core.config.interactor.usecase.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.template.IDeleteTemplateService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteTemplate extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteTemplate {
  
  @Autowired
  protected IDeleteTemplateService deleteTemplateService;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel templateModel) throws Exception
  {
    return deleteTemplateService.execute(templateModel);
  }
}
