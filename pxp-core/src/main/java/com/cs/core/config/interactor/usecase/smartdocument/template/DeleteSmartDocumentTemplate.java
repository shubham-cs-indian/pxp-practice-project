package com.cs.core.config.interactor.usecase.smartdocument.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.smartdocument.template.IDeleteSmartDocumentTemplateService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class DeleteSmartDocumentTemplate extends AbstractDeleteConfigInteractor<IIdParameterModel, IBulkDeleteReturnModel>
    implements IDeleteSmartDocumentTemplate {
  
  @Autowired
  protected IDeleteSmartDocumentTemplateService deleteSmartDocumentTemplateService;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return deleteSmartDocumentTemplateService.execute(dataModel);
  }
}
