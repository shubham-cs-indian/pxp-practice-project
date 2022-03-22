package com.cs.core.runtime.interactor.usecase.workflow.processinstance;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.asset.GetAssetDetailsResponseModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;

@Component
public class GetPXONExportFile extends AbstractRuntimeInteractor<IIdParameterModel, IGetAssetDetailsResponseModel> implements IGetPXONExportFile{

  @Override
  public IGetAssetDetailsResponseModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    IGetAssetDetailsResponseModel fileModel = new GetAssetDetailsResponseModel();
    try {
      String directory = CSProperties.instance().getString("bgp.PXON_EXPORT.filePath");
      Path path = FileSystems.getDefault().getPath(directory, "export#" + dataModel.getId() + ".pxon");
      File file = path.toFile();
      fileModel.setContentType("application/octet-stream");
      fileModel.setContentDisposition("attachment; filename=\"export#"+dataModel.getId()+".pxon\"");
      fileModel.setInputStream(new FileInputStream(file));
    }
    catch (IOException | CSInitializationException ex) {
      RDBMSLogger.instance().exception(ex);
      throw new RuntimeException(ex.getMessage());
    }
    
    return fileModel;
  }
  
}
