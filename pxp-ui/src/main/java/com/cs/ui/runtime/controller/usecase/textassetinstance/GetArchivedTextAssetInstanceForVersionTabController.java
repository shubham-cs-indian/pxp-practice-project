package com.cs.ui.runtime.controller.usecase.textassetinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestModel;
import com.cs.dam.runtime.interactor.version.IGetArchivedAssetInstanceForVersionTab;
import com.cs.pim.runtime.interactor.usecase.textassetinstance.IGetArchivedTextAssetInstanceForVersionTab;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/runtime")
public class GetArchivedTextAssetInstanceForVersionTabController extends BaseController
  implements IRuntimeController {
  
  @Autowired
  protected IGetArchivedTextAssetInstanceForVersionTab getArchivedTextAssetInstanceForVersionTab;
  
  @RequestMapping(value = "/archivedtextassetinstances/timelinetab/{id}", method = RequestMethod.POST)
  public IRESTModel execute(@PathVariable String id, @RequestBody GetInstanceRequestModel  model) throws Exception
  {
    model.setId(id);
    return createResponse(getArchivedTextAssetInstanceForVersionTab.execute(model));
  }
}
