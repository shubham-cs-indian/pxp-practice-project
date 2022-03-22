package com.cs.dam.runtime.interactor.usecase.smartdocument;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.smartdocument.IGetConfigDetailsWithoutPermissionsForSmartDocumentStrategy;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.smartdocument.IMulticlassificationRequestModelForSmartDocument;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsWithoutPermissionsForSmartDocumentStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsWithoutPermissionsForSmartDocumentStrategy {
  
  @Autowired
  protected ISessionContext context;
  
  @Override
  public IGetConfigDetailsForCustomTabModel execute(
      IMulticlassificationRequestModelForSmartDocument model) throws Exception
  {
    String userId = context.getUserId();
    model.setUserId(userId);
    return execute(OrientDBBaseStrategy.GET_CONFIG_DETAILS_WITHOUT_PERMISSIONS_FOR_SMART_DOCUMENT,
        model, GetConfigDetailsForCustomTabModel.class);
  }
}
