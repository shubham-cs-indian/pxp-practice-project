package com.cs.core.config.interactor.usecase.smartdocument.preset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.config.smartdocument.preset.ICreateSmartDocumentPresetService;

@Service
public class CreateSmartDocumentPreset extends AbstractCreateConfigInteractor<ISmartDocumentPresetModel, IGetSmartDocumentPresetModel>
    implements ICreateSmartDocumentPreset {
  
  @Autowired
  protected ICreateSmartDocumentPresetService createSmartDocumentPresetService;
  
  @Override
  public IGetSmartDocumentPresetModel executeInternal(ISmartDocumentPresetModel dataModel) throws Exception
  {
    return createSmartDocumentPresetService.execute(dataModel);
  }
}
