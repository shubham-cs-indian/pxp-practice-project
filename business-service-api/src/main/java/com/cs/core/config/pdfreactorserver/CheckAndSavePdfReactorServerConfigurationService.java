package com.cs.core.config.pdfreactorserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentWithTemplateModel;
import com.cs.core.config.interactor.model.smartdocument.ISmartDocumentModel;
import com.cs.core.config.strategy.usecase.smartdocument.ISaveSmartDocumentStrategy;
import com.cs.core.services.CSPDFReactorServer;

/**
 * This is service class to connect to PDF reactor server and save pdf reactor
 * server details in OrientDB
 * 
 * @author pranav.huchche
 */
@Service
public class CheckAndSavePdfReactorServerConfigurationService extends AbstractGetConfigService<ISmartDocumentModel, IGetSmartDocumentWithTemplateModel>
    implements ICheckAndSavePdfReactorServerConfigurationService {
  
  @Autowired
  protected ISaveSmartDocumentStrategy saveSmartDocumentStrategy;
  
  @Override
  public IGetSmartDocumentWithTemplateModel executeInternal(ISmartDocumentModel dataModel) throws Exception
  {
    // Fetch IP, lisence and port from request
    String redererHostIp = dataModel.getRendererHostIp();
    String redererPort = dataModel.getRendererPort();
    
    // Connect to PDF reactor server
    CSPDFReactorServer.createInstance(redererHostIp, redererPort);
    
    // Save details in Orient and return smart document node details
    return saveSmartDocumentStrategy.execute(dataModel);
  }
  
}
