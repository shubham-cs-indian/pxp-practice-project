package com.cs.ui.config.controller.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.di.config.interactor.authorization.IDeletePartnerAuthorization;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class DeletePartnerAuthorizationController extends BaseController implements IConfigController {

  @Autowired
  protected IDeletePartnerAuthorization deleteAuthorizationMapping;
  
  @RequestMapping(value = "/authorizationmappings", method = RequestMethod.DELETE)
  public IRESTModel execute( @RequestBody IdsListParameterModel bulkIdsModel) throws Exception
  {
    return createResponse(deleteAuthorizationMapping.execute(bulkIdsModel));
  }
}
