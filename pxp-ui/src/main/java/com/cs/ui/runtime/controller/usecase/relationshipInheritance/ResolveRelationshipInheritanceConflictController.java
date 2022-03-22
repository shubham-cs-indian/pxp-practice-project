package com.cs.ui.runtime.controller.usecase.relationshipInheritance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.entity.relationshipinstance.ResolveRelationshipConflictsRequestModel;
import com.cs.core.runtime.interactor.usecase.relationshipinstance.IResolveRelationshipInheritanceConflict;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class ResolveRelationshipInheritanceConflictController extends BaseController implements IRuntimeController{
  
  @Autowired
  protected IResolveRelationshipInheritanceConflict resolveRelationshipInheritanceConflict;
  
  @RequestMapping(value = "/resolve/relationshipinheritanceconflict", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ResolveRelationshipConflictsRequestModel  model) throws Exception
  {
    return createResponse(resolveRelationshipInheritanceConflict.execute(model));
  }
}