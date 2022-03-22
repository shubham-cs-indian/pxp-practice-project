package com.cs.core.config.interactor.usecase.smartdocument.preset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISaveSmartDocumentPresetModel;
import com.cs.core.config.smartdocument.preset.ISaveSmartDocumentPresetService;

@Service
public class SaveSmartDocumentPreset extends AbstractSaveConfigInteractor<ISaveSmartDocumentPresetModel, IGetSmartDocumentPresetModel>
    implements ISaveSmartDocumentPreset {
  
  @Autowired
  protected ISaveSmartDocumentPresetService saveSmartDocumentPresetService;
  
  @Override
  public IGetSmartDocumentPresetModel executeInternal(ISaveSmartDocumentPresetModel dataModel) throws Exception
  {
    return saveSmartDocumentPresetService.execute(dataModel);
  }
}
