package com.cs.core.config.smartdocument.preset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.strategy.usecase.smartdocument.preset.IGetSmartDocumentPresetStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetSmartDocumentPresetService extends AbstractGetConfigService<IIdParameterModel, IGetSmartDocumentPresetModel>
    implements IGetSmartDocumentPresetService {
  
  @Autowired
  protected IGetSmartDocumentPresetStrategy getSmartDocumentPresetStrategy;
  
  @Override
  public IGetSmartDocumentPresetModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getSmartDocumentPresetStrategy.execute(dataModel);
  }
}
