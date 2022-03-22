package com.cs.core.config.interactor.usecase.smartdocument.preset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.smartdocument.preset.IGetSmartDocumentPresetService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetSmartDocumentPreset extends AbstractGetConfigInteractor<IIdParameterModel, IGetSmartDocumentPresetModel>
    implements IGetSmartDocumentPreset {
  
  @Autowired
  protected IGetSmartDocumentPresetService getSmartDocumentPresetService;
  
  @Override
  public IGetSmartDocumentPresetModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getSmartDocumentPresetService.execute(dataModel);
  }
}
