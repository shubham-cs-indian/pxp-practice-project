package com.cs.core.config.smartdocument.preset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetAllSmartDocumentPresetResponseModel;
import com.cs.core.config.strategy.usecase.smartdocument.preset.IGetAllSmartDocumentPresetStrategy;

@Service
public class GetAllSmartDocumentPresetService
    extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetAllSmartDocumentPresetResponseModel>
    implements IGetAllSmartDocumentPresetService {
  
  @Autowired
  protected IGetAllSmartDocumentPresetStrategy getAllSmartDocumentPresetStrategy;
  
  @Override
  public IGetAllSmartDocumentPresetResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllSmartDocumentPresetStrategy.execute(dataModel);
  }
}
