package com.cs.core.config.strategy.usecase.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.GetSmartDocumentInfoModel;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentInfoModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.strategy.usecase.smartdocument.IGetSmartDocumentConfigInfoStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetSmartDocumentConfigInfoStrategy extends OrientDBBaseStrategy
    implements IGetSmartDocumentConfigInfoStrategy {
  
  @Override
  public IGetSmartDocumentInfoModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_SMART_DOCUMENT_CONFIG_INFO, model, GetSmartDocumentInfoModel.class);
  }
}
