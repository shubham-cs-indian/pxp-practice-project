package com.cs.ui.runtime.controller.usecase.assetinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestModel;
import com.cs.core.runtime.interactor.usecase.assetinstance.IGetAssetInstanceForOverviewTab;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/runtime")
public class GetAssetInstanceForOverviewTabController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetAssetInstanceForOverviewTab getAssetInstanceForOverviewTab;
  
  @RequestMapping(value = "/assetinstances/overviewtab/{id}", method = RequestMethod.POST)
  public IRESTModel execute(@PathVariable String id, @RequestBody GetInstanceRequestModel model)
      throws Exception
  {
    model.setId(id);
    return createResponse(getAssetInstanceForOverviewTab.execute(model));
  }
}
