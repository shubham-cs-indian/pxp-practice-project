package com.cs.core.config.interactor.usecase.smartdocument.preset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.smartdocument.preset.IDeleteSmartDocumentPresetService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class DeleteSmartDocumentPreset extends AbstractDeleteConfigInteractor<IIdParameterModel, IBulkDeleteReturnModel>
    implements IDeleteSmartDocumentPreset {
  
  @Autowired
  protected IDeleteSmartDocumentPresetService getSmartDocumentPresetService;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getSmartDocumentPresetService.execute(dataModel);
  }
}
