package com.cs.ui.config.controller.usecase.relationship;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.relationship.CreateRelationshipModel;
import com.cs.core.config.interactor.usecase.relationship.ICreateRelationship;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateRelationshipController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateRelationship createRelationshipInteractor;
  
  @RequestMapping(value = "/relationships", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateRelationshipModel dataTransferModel) throws Exception
  {
    return createResponse(createRelationshipInteractor.execute(dataTransferModel));
  }
}
