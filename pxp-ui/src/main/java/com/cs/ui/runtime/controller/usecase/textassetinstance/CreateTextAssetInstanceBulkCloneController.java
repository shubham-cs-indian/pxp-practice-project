package com.cs.ui.runtime.controller.usecase.textassetinstance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.klassinstance.CreateKlassInstanceBulkCloneModel;
import com.cs.pim.runtime.interactor.usecase.textassetinstance.ICreateTextAssetInstanceBulkClone;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class CreateTextAssetInstanceBulkCloneController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected ICreateTextAssetInstanceBulkClone createTextAssetInstanceBulkClone;
  
  @RequestMapping(value = "/textassetinstance/bulkclone", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateKlassInstanceBulkCloneModel createCloneModel) throws Exception
  {
    return createResponse(createTextAssetInstanceBulkClone.execute(createCloneModel));
  }

}