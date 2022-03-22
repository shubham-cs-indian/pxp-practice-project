package com.cs.ui.runtime.controller.usecase.dataIntegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instance.IdPaginationModel;
import com.cs.di.runtime.interactor.usecase.dataIntegration.IGetImportedFilesForEndpoint;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
public class GetImportedFilesForEndpointController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetImportedFilesForEndpoint getImportedFilesForEndpoint;
  
  @RequestMapping(value = "/getuploadedfilesforendpointinfo", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody IdPaginationModel requestModel) throws Exception
  {
    return createResponse(getImportedFilesForEndpoint.execute(requestModel));
  }
}
