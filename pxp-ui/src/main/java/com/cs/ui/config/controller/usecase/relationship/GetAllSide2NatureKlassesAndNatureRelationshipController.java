package com.cs.ui.config.controller.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.relationship.IGetAllSide2NatureKlassesAndNatureRelationship;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class GetAllSide2NatureKlassesAndNatureRelationshipController extends BaseController implements IConfigController {
 
  @Autowired
  protected IGetAllSide2NatureKlassesAndNatureRelationship getAllSide2NatureKlassFromNatureRelationship;
  
  @RequestMapping(value = "/getAllSide2NatureKlassFromNatureRelationship/{natureRelationshipId}", method = RequestMethod.GET)
  public IRESTModel getRelationship(@PathVariable String natureRelationshipId) throws Exception
  {
    return createResponse(getAllSide2NatureKlassFromNatureRelationship.execute(new IdParameterModel(natureRelationshipId)));
  }
}
