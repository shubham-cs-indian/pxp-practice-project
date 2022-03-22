package com.cs.core.config.interactor.usecase.assetserver;

import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.constants.DAMConstants;
import com.cs.core.config.interactor.model.asset.GetAssetDetailsResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.strategy.utils.DownloadUtils;
import com.cs.core.services.CSProperties;

@Service
public class BulkAssetDownload
    extends AbstractRuntimeInteractor<IIdParameterModel, IGetAssetDetailsResponseModel>
    implements IBulkAssetDownload {
  
  @Autowired
  ISessionContext context;
  
  @Override
  public IGetAssetDetailsResponseModel executeInternal(IIdParameterModel requestModel) throws Exception
  {
    GetAssetDetailsResponseModel fileModel = new GetAssetDetailsResponseModel();
    String assetFilesPath = CSProperties.instance().getString("nfs.file.path");
    String filePath = context.getFromIdFilePathMap(assetFilesPath + DAMConstants.PATH_DELIMINATOR + requestModel.getId());
    String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
    String extension = filePath.substring(filePath.lastIndexOf('.'));
    InputStream iStream = new FileInputStream(filePath);
    fileModel.setInputStream(iStream);
    if (extension.equals(".zip")) {
      fileModel.setContentDisposition(CommonConstants.CONTENT_ATTACHMENT + fileName + "\"");
      fileModel.setContentType(CommonConstants.CONTENT_TYPE_APPLICATION + "zip");
    }
    else {
      fileModel.setContentDisposition(CommonConstants.CONTENT_ATTACHMENT + fileName + "\"");
      fileModel.setContentType(DownloadUtils.getContentType(extension));
    }
    
    return fileModel;
  }
}
