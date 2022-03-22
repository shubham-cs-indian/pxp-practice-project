package com.cs.core.config.smartdocument.preset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.config.strategy.usecase.smartdocument.preset.IGetAllSmartDocumentPresetForTemplateStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllSmartDocumentPresetForTemplateService
    extends AbstractGetConfigService<IIdParameterModel, IListModel<ISmartDocumentPresetModel>>
    implements IGetAllSmartDocumentPresetForTemplateService {
  
  @Autowired
  protected IGetAllSmartDocumentPresetForTemplateStrategy getAllSmartDocumentPresetForTemplateStrategy;
  
  @Override
  public IListModel<ISmartDocumentPresetModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAllSmartDocumentPresetForTemplateStrategy.execute(dataModel);
  }
}
