package com.cs.ui.runtime.controller.usecase.assetinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInstanceSaveModel;
import com.cs.core.runtime.interactor.usecase.assetinstance.ISaveAssetInstanceForOverviewTab;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/runtime")
public class SaveAssetInstanceForOverviewTabController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  ISaveAssetInstanceForOverviewTab saveAssetInstanceForOverviewTab;
  
  @RequestMapping(value = "/assetinstances/tabs/overviewtab", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody AssetInstanceSaveModel klassInstanceModels,
      @RequestParam(required = false) String versionClassId) throws Exception
  {
    return createResponse(saveAssetInstanceForOverviewTab.execute(klassInstanceModels));
  }
}
