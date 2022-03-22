package com.cs.ui.config.controller.usecase.relationship;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.relationship.IGetRelationship;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetRelationshipController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetRelationship getRelationshipInteractor;
  
  @RequestMapping(value = "/relationships/{id}", method = RequestMethod.GET)
  public IRESTModel getRelationship(@PathVariable String id) throws Exception
  {
    IIdParameterModel getEntityModel = new IdParameterModel(id);
    
    return createResponse(getRelationshipInteractor.execute(getEntityModel));
  }
}
