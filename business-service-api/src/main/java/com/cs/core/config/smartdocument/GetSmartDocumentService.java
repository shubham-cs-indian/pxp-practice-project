package com.cs.core.config.smartdocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.strategy.usecase.smartdocument.IGetSmartDocumentStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetSmartDocumentService extends AbstractGetConfigService<IIdParameterModel, IGetSmartDocumentWithTemplateModel>
    implements IGetSmartDocumentService {
  
  @Autowired
  protected IGetSmartDocumentStrategy getSmartDocumentStrategy;
  
  @Override
  public IGetSmartDocumentWithTemplateModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getSmartDocumentStrategy.execute(dataModel);
  }
}
