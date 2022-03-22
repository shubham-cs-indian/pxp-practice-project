package com.cs.ui.runtime.controller.usecase.marketinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.GetVariantInstancesInTableViewRequestModel;
import com.cs.core.runtime.interactor.usecase.variant.marketinstance.IGetMarketVariantInstancesInTableView;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;


@RestController
@RequestMapping(value = "/runtime")
public class GetMarketVariantInstancesInTableViewController extends BaseController
implements IRuntimeController {
  
  @Autowired
  protected IGetMarketVariantInstancesInTableView getMarketVariantInstancesInTableView;
  
  @RequestMapping(value = "/marketinstance/variant/tableview", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetVariantInstancesInTableViewRequestModel model) throws Exception
  {
    return createResponse(getMarketVariantInstancesInTableView.execute(model));
  }
}
