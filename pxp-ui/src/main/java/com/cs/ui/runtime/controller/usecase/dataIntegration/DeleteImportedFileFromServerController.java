package com.cs.ui.runtime.controller.usecase.dataIntegration;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.di.runtime.interactor.usecase.dataIntegration.IDeleteImportedFileFromServer;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
public class DeleteImportedFileFromServerController extends BaseController {
  
  @Autowired
  protected IDeleteImportedFileFromServer deleteImportedFileFromServer;
  
  @RequestMapping(value = "/deleteimportedfile", method = RequestMethod.DELETE)
  public IRESTModel deleteAssetFromServer(@RequestBody ArrayList<String> fileIds) throws Exception
  {
    IListModel<String> requestModel = new ListModel<>(fileIds);
    return createResponse(deleteImportedFileFromServer.execute(requestModel));
  }
}
