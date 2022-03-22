package com.cs.ui.runtime.controller.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.relationship.SaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.usecase.textassetinstance.ISaveTextAssetInstanceRelationships;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;


@RestController
@RequestMapping(value = "/runtime")
public class SaveTextAssetInstanceRelationshipController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected ISaveTextAssetInstanceRelationships saveTextAssetInstanceRelationships;
  
  @RequestMapping(value = "/textassetinstance/relationshipinstances", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveRelationshipInstanceModel klassInstanceModels) throws Exception
  {
    return createResponse(saveTextAssetInstanceRelationships.execute(klassInstanceModels));
  }
}
