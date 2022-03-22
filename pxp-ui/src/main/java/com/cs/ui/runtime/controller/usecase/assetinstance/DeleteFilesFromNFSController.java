package com.cs.ui.runtime.controller.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.interactor.usecase.assetserver.IDeleteFilesFromNFS;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * This controller deletes the files from the given paths.
 * @author vannya.kalani
 *
 */
@RestController
public class DeleteFilesFromNFSController extends BaseController {
  
  @Autowired
  protected IDeleteFilesFromNFS deleteFilesFromNFS;
  
  @DeleteMapping(value = "/nfsfiles")
  public IRESTModel deleteAssetFromServer(@RequestBody IdsListParameterModel filePaths) throws Exception
  {
    return createResponse(deleteFilesFromNFS.execute(filePaths));
  }
}
