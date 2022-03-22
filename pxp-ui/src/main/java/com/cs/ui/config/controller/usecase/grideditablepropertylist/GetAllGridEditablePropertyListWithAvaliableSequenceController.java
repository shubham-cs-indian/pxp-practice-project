package com.cs.ui.config.controller.usecase.grideditablepropertylist;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.grideditpropertylist.GetGridEditablePropertyListRequestModel;
import com.cs.core.config.interactor.usecase.grideditpropertylist.IGetAllGridEditablePropertyListWithAvaliableSequence;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllGridEditablePropertyListWithAvaliableSequenceController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetAllGridEditablePropertyListWithAvaliableSequence getAllGridEditablePropertyListWithAvaliableSequence;
  
  @RequestMapping(value = "/grideditpropertylists", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetGridEditablePropertyListRequestModel model)
      throws Exception
  {
    
    return createResponse(getAllGridEditablePropertyListWithAvaliableSequence.execute(model));
  }
}
