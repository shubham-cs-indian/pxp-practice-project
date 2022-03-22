package com.cs.core.config.smartdocument.preset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.config.strategy.usecase.smartdocument.preset.ICreateSmartDocumentPresetStrategy;

@Service
public class CreateSmartDocumentPresetService extends AbstractCreateConfigService<ISmartDocumentPresetModel, IGetSmartDocumentPresetModel>
    implements ICreateSmartDocumentPresetService {
  
  @Autowired
  protected ICreateSmartDocumentPresetStrategy createSmartDocumentPresetStrategy;
  
  @Override
  public IGetSmartDocumentPresetModel executeInternal(ISmartDocumentPresetModel dataModel) throws Exception
  {
    return createSmartDocumentPresetStrategy.execute(dataModel);
  }
}
