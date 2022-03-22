package com.cs.ui.runtime.controller.usecase.marketinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.GetPropertiesVariantInstancesInTableViewRequestModel;
import com.cs.core.runtime.interactor.usecase.variant.marketinstance.IGetMarketPropertiesVariantInstancesInTableView;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;



@RestController
@RequestMapping(value = "/runtime")
public class GetMarketPropertiesVariantInstancesInTableViewController extends BaseController
implements IRuntimeController {
  
  @Autowired
  protected IGetMarketPropertiesVariantInstancesInTableView getMarketPropertiesVariantInstancesInTableView;
  
  @RequestMapping(value = "/marketinstance/properties/variant/tableview", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetPropertiesVariantInstancesInTableViewRequestModel model) throws Exception
  {
    return createResponse(getMarketPropertiesVariantInstancesInTableView.execute(model));
  }
}
