package com.cs.core.config.smartdocument.preset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.config.strategy.usecase.smartdocument.preset.IDeleteSmartDocumentPresetStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class DeleteSmartDocumentPresetService extends AbstractDeleteConfigService<IIdParameterModel, IBulkDeleteReturnModel>
    implements IDeleteSmartDocumentPresetService {
  
  @Autowired
  protected IDeleteSmartDocumentPresetStrategy getSmartDocumentPresetStrategy;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getSmartDocumentPresetStrategy.execute(dataModel);
  }
}
