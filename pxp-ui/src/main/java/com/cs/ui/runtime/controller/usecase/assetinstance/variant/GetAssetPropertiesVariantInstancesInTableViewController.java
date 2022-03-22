package com.cs.ui.runtime.controller.usecase.assetinstance.variant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.GetPropertiesVariantInstancesInTableViewRequestModel;
import com.cs.core.runtime.interactor.usecase.variant.assetinstance.IGetAssetPropertiesVariantInstancesInTableView;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAssetPropertiesVariantInstancesInTableViewController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetAssetPropertiesVariantInstancesInTableView getAssetPropertiesVariantInstancesInTableView;
  
  @RequestMapping(value = "/assetinstance/properties/variant/tableview",
      method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetPropertiesVariantInstancesInTableViewRequestModel model)
      throws Exception
  {
    return createResponse(getAssetPropertiesVariantInstancesInTableView.execute(model));
  }
}
