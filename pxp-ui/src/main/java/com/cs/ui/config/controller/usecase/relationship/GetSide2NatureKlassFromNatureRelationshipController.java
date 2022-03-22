package com.cs.ui.config.controller.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.relationship.IGetSide2NatureKlassFromNatureRelationship;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetSide2NatureKlassFromNatureRelationshipController extends BaseController implements IConfigController {
 
  @Autowired
  protected IGetSide2NatureKlassFromNatureRelationship getSide2NatureKlassFromNatureRelationship;
  
  @RequestMapping(value = "/side2NatureKlassFromNatureRelationship/{natureRelationshipId}", method = RequestMethod.GET)
  public IRESTModel getRelationship(@PathVariable String natureRelationshipId) throws Exception
  {
    IIdParameterModel requestModel = new IdParameterModel(natureRelationshipId);
    return createResponse(getSide2NatureKlassFromNatureRelationship.execute(requestModel));
  }
}
