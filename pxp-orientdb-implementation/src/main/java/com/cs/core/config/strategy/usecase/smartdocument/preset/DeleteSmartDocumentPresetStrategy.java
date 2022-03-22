package com.cs.core.config.strategy.usecase.smartdocument.preset;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component("deleteSmartDocumentPresetStrategy")
public class DeleteSmartDocumentPresetStrategy extends OrientDBBaseStrategy
    implements IDeleteSmartDocumentPresetStrategy {
  
  @Override
  public IBulkDeleteReturnModel execute(IIdParameterModel model) throws Exception
  {
    return execute(DELETE_SMART_DOCUMENT_PRESET, model, BulkDeleteReturnModel.class);
  }
}
