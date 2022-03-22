package com.cs.ui.config.controller.usecase.relationship;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.klass.IGetKlassesForRelationship;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetKlassesForRelationshipController extends BaseController
    implements IConfigController {
  
  @Autowired
  IGetKlassesForRelationship getKlassesForRelationshipInteractor;
  
  @RequestMapping(value = "/klassesForRelationship", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getKlassesForRelationshipInteractor.execute(null));
  }
}
