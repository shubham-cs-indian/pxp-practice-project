package com.cs.ui.config.controller.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;
import com.cs.core.config.interactor.usecase.relationship.IGetEligibleRelationshipsForRelationshipInheritance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;



@RestController
@RequestMapping(value = "/config")
public class GetEligibleRelationshipsForRelationshipInheritanceController extends BaseController implements IConfigController{
    
    @Autowired
    protected IGetEligibleRelationshipsForRelationshipInheritance getEligibleRelationshipsForRelationshipInheritance;
    
    @RequestMapping(value = "/relationships/inheritance", method = RequestMethod.POST)
    public IRESTModel getRelationship(@RequestBody ConfigGetAllRequestModel model) throws Exception
    {
      return createResponse(getEligibleRelationshipsForRelationshipInheritance.execute(model));
    }
}

