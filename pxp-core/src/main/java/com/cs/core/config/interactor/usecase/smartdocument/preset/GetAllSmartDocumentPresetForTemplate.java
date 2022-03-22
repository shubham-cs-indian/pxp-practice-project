package com.cs.core.config.interactor.usecase.smartdocument.preset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.config.smartdocument.preset.IGetAllSmartDocumentPresetForTemplateService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllSmartDocumentPresetForTemplate
    extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<ISmartDocumentPresetModel>>
    implements IGetAllSmartDocumentPresetForTemplate {
  
  @Autowired
  protected IGetAllSmartDocumentPresetForTemplateService getAllSmartDocumentPresetForTemplateService;
  
  @Override
  public IListModel<ISmartDocumentPresetModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAllSmartDocumentPresetForTemplateService.execute(dataModel);
  }
}
