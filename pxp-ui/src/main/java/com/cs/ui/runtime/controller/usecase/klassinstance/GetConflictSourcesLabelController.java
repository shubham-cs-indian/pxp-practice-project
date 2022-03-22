package com.cs.ui.runtime.controller.usecase.klassinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.klassinstance.ConflictSourcesRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConflictSourcesInformation;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class GetConflictSourcesLabelController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IGetConflictSourcesInformation getConflictSourcesLabel;
  
  @RequestMapping(value = "/getconflictsourcesinfo", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ConflictSourcesRequestModel model) throws Exception
  {
    return createResponse(getConflictSourcesLabel.execute(model));
  }
}
