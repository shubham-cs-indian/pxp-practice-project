package com.cs.core.config.smartdocument.preset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISaveSmartDocumentPresetModel;
import com.cs.core.config.strategy.usecase.smartdocument.preset.ISaveSmartDocumentPresetStrategy;

@Service
public class SaveSmartDocumentPresetService extends AbstractSaveConfigService<ISaveSmartDocumentPresetModel, IGetSmartDocumentPresetModel>
    implements ISaveSmartDocumentPresetService {
  
  @Autowired
  protected ISaveSmartDocumentPresetStrategy saveSmartDocumentPresetStrategy;
  
  @Override
  public IGetSmartDocumentPresetModel executeInternal(ISaveSmartDocumentPresetModel dataModel) throws Exception
  {
    return saveSmartDocumentPresetStrategy.execute(dataModel);
  }
}
