package com.cs.ui.runtime.controller.usecase.articleinstance.variant;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.GetPropertiesVariantInstancesInTableViewRequestModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.variant.IGetArticlePropertiesVariantInstancesInTableView;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class GetArticlePropertiesVariantInstancesInTableViewController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetArticlePropertiesVariantInstancesInTableView getArticlePropertiesVariantInstancesInTableView;
  
  @RequestMapping(value = "/articleinstance/properties/variant/tableview",
      method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetPropertiesVariantInstancesInTableViewRequestModel model)
      throws Exception
  {
    return createResponse(getArticlePropertiesVariantInstancesInTableView.execute(model));
  }
}
