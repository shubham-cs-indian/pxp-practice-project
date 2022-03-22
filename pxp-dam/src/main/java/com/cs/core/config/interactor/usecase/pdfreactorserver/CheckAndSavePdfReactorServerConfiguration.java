package com.cs.core.config.interactor.usecase.pdfreactorserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.interactor.model.smartdocument.ISmartDocumentModel;
import com.cs.core.config.pdfreactorserver.ICheckAndSavePdfReactorServerConfigurationService;

/**
 * This is service class to connect to PDF reactor server and save pdf reactor
 * server details in OrientDB
 * 
 * @author pranav.huchche
 */
@Service
public class CheckAndSavePdfReactorServerConfiguration extends AbstractGetConfigInteractor<ISmartDocumentModel, IGetSmartDocumentWithTemplateModel>
    implements ICheckAndSavePdfReactorServerConfiguration {
  
  @Autowired
  protected ICheckAndSavePdfReactorServerConfigurationService checkAndSavePdfReactorServerConfigurationService;
  
  @Override
  public IGetSmartDocumentWithTemplateModel executeInternal(ISmartDocumentModel dataModel) throws Exception
  {
    return checkAndSavePdfReactorServerConfigurationService.execute(dataModel);
  }
  
}
