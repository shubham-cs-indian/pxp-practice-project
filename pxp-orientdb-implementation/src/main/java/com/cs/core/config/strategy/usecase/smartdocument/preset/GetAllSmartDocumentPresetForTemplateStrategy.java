package com.cs.core.config.strategy.usecase.smartdocument.preset;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.SmartDocumentPresetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component("getAllSmartDocumentPresetForTemplateStrategy")
public class GetAllSmartDocumentPresetForTemplateStrategy extends OrientDBBaseStrategy
    implements IGetAllSmartDocumentPresetForTemplateStrategy {
  
  @Override
  public IListModel<ISmartDocumentPresetModel> execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_ALL_SMART_DOCUMENT_PRESET_BY_TEMPLATE, model,
        new TypeReference<ListModel<SmartDocumentPresetModel>>()
        {
          
        });
  }
}
