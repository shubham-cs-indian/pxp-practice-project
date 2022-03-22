package com.cs.ui.runtime.controller.usecase.transfer;


import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.di.runtime.interactor.model.transfer.TransferEntityRequestModel;
import com.cs.di.runtime.interactor.transfer.IManualTransferInteractor;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class ManualTransferController extends BaseController implements IRuntimeController {
 
  @Autowired
  protected IManualTransferInteractor manualTransferInteractor;
  
  @RequestMapping(value = "/manualtransfer", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody TransferEntityRequestModel requestModel, @QueryParam(value = "physicalCatalogId") String physicalCatalogId) throws Exception
  {
    requestModel.setSourceCatalogId(physicalCatalogId);
    return createResponse(manualTransferInteractor.execute(requestModel));
  }
}