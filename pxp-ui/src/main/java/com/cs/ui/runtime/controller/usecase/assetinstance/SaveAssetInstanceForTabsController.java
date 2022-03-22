package com.cs.ui.runtime.controller.usecase.assetinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInstanceSaveModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ISaveAssetInstanceForTabs;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/runtime")
public class SaveAssetInstanceForTabsController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  ISaveAssetInstanceForTabs saveAssetInstanceForTabs;
  
  @RequestMapping(value = "/assetinstances/tabs", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody AssetInstanceSaveModel klassInstanceModels,
      @RequestParam(required = false) String versionClassId) throws Exception
  {
    return createResponse(saveAssetInstanceForTabs.execute(klassInstanceModels));
  }
}
