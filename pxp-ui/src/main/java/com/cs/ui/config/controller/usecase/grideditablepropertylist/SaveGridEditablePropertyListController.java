package com.cs.ui.config.controller.usecase.grideditablepropertylist;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.grideditpropertylist.SaveGridEditablePropertyListModel;
import com.cs.core.config.interactor.usecase.grideditpropertylist.ISaveGridEditablePropertyList;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveGridEditablePropertyListController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected ISaveGridEditablePropertyList saveGridEditablePropertyList;
  
  @RequestMapping(value = "/grideditpropertylist", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveGridEditablePropertyListModel model) throws Exception
  {
    
    return createResponse(saveGridEditablePropertyList.execute(model));
  }
  
}
