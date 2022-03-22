package com.cs.ui.config.controller.usecase.tabs;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.tabs.CreateTabModel;
import com.cs.core.config.interactor.usecase.tab.ICreateTab;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateTabController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateTab createTab;
  
  @RequestMapping(value = "/tab", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateTabModel condition) throws Exception
  {
    return createResponse(createTab.execute(condition));
  }
}
