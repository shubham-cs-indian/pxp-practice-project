package com.cs.ui.runtime.controller.usecase.assetinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.bulkpropagation.BulkCreateAssetInstanceModel;
import com.cs.runtime.interactor.usecase.base.assetInstance.IBulkCreateAssetInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class BulkCreateAssetInstanceController extends BaseController
    implements IRuntimeController {
  
  @Autowired
    protected IBulkCreateAssetInstance createAssetInstance;
  
  @RequestMapping(value = "/bulkcreateassetinstances", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody BulkCreateAssetInstanceModel bulkAssetInstances)
      throws Exception
  {
    return createResponse(createAssetInstance.execute(bulkAssetInstances));
  }
}
