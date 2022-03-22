package com.cs.ui.runtime.controller.usecase.assetserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.constants.DAMConstants;
import com.cs.core.config.interactor.usecase.assetserver.IBulkAssetDownload;
import com.cs.core.runtime.interactor.model.assetinstance.BulkAssetDownloadWithVariantsModel;
import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.services.CSProperties;
import com.cs.dam.runtime.interactor.usecase.assetinstance.IGetBulkAssetObjectId;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
public class BulkAssetDownloadFromServerController extends BaseController {
  
  public static final String RESPONSE_HEADER_CONTENT_DISPOSITION = "Content-Disposition";
  public static final String RESPONSE_HEADER_CONTENT_LENGTH      = "Content-Length";
  
  @Autowired
  ISessionContext            springSessionContext;
  
  @Autowired
  IGetBulkAssetObjectId      getFileIdOfAsset;
  
  @Autowired
  IBulkAssetDownload         getAssetInstanceById;
  
  @RequestMapping(value = "/assetinstances/getFileId", method = RequestMethod.POST)
  public IRESTModel getBulkAssetObjectId(@RequestBody BulkAssetDownloadWithVariantsModel model)
      throws Exception
  {
    return createResponse(getFileIdOfAsset.execute(model));
  }
  
  @RequestMapping(value = "/assetinstances/getFileById/{id}", method = RequestMethod.GET)
  public void getMultipleAssetsFromServer(@PathVariable String id, HttpServletResponse response)
      throws Exception
  {
    IdParameterModel model = new IdParameterModel();
    model.setId(id);
    IGetAssetDetailsResponseModel fileModel = getAssetInstanceById.execute(model);
    BufferedInputStream input = null;
    BufferedOutputStream output = null;
    
    response.addHeader(RESPONSE_HEADER_CONTENT_DISPOSITION, fileModel.getContentDisposition());
    response.setContentType(fileModel.getContentType());
    try {
      input = new BufferedInputStream(fileModel.getInputStream());
      output = new BufferedOutputStream(response.getOutputStream());
      byte[] buffer = new byte[8192];
      for (int length = 0; (length = input.read(buffer)) > 0;) {
        output.write(buffer, 0, length);
      }
    }
    finally {
      if (output != null)
        try {
          output.close();
        }
        catch (IOException logOrIgnore) {
        }
      if (input != null)
        try {
          input.close();
        }
        catch (IOException logOrIgnore) {
        }
    }

    String assetFilesPath = CSProperties.instance().getString("nfs.file.path");
    String filePath = springSessionContext.getFromIdFilePathMap(assetFilesPath + DAMConstants.PATH_DELIMINATOR + id);
    File fileToDelete = new File(filePath);
    fileToDelete.delete();
    File folderToDelete = new File(assetFilesPath + DAMConstants.PATH_DELIMINATOR + id);
    folderToDelete.delete();
    springSessionContext.removeFromIdFilePathMap(assetFilesPath + DAMConstants.PATH_DELIMINATOR + id);
  }
}
