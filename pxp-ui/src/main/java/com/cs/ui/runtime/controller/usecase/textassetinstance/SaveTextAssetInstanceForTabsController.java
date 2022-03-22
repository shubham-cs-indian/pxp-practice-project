package com.cs.ui.runtime.controller.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.textassetinstance.TextAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.usecase.textassetinstance.ISaveTextAssetInstanceForTabs;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;



@RestController
@RequestMapping(value = "/runtime")
public class SaveTextAssetInstanceForTabsController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected ISaveTextAssetInstanceForTabs saveTextAssetInstanceForTabs;
  
  @RequestMapping(value = "/textassetinstances/tabs", method = RequestMethod.POST)
  public IRESTModel execute(
      @RequestBody TextAssetInstanceSaveModel klassInstanceModels,
      @RequestParam(required=false) String versionClassId) throws Exception
  {
    return createResponse(saveTextAssetInstanceForTabs.execute(klassInstanceModels));
  }
}
