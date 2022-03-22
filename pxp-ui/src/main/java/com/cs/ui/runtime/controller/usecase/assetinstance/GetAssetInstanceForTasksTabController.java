package com.cs.ui.runtime.controller.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestModel;
import com.cs.dam.runtime.interactor.usecase.assetinstance.IGetAssetInstanceForTasksTab;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAssetInstanceForTasksTabController extends BaseController implements IRuntimeController  {
  
  @Autowired
  protected IGetAssetInstanceForTasksTab getAssetInstanceForTasksTab;
  
  @RequestMapping(value = "/assetinstances/tasktab/{id}", method = RequestMethod.POST)
  public IRESTModel execute(@PathVariable String id,@RequestParam(required = false, defaultValue = "false")  Boolean getAll,
	    @RequestParam(required = false, defaultValue = "false") Boolean count,
	    @RequestBody GetInstanceRequestModel model) throws Exception {
      
    model.setIsGetAll(getAll);
    model.setId(id);
    return createResponse(getAssetInstanceForTasksTab.execute(model));
  }
}
