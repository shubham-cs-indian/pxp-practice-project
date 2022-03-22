package com.cs.core.config.interactor.usecase.smartdocument.preset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetAllSmartDocumentPresetResponseModel;
import com.cs.core.config.smartdocument.preset.IGetAllSmartDocumentPresetService;

@Service
public class GetAllSmartDocumentPreset extends
    AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetAllSmartDocumentPresetResponseModel> implements IGetAllSmartDocumentPreset {
  
  @Autowired
  protected IGetAllSmartDocumentPresetService getAllSmartDocumentPresetService;
  
  @Override
  public IGetAllSmartDocumentPresetResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllSmartDocumentPresetService.execute(dataModel);
  }
}
