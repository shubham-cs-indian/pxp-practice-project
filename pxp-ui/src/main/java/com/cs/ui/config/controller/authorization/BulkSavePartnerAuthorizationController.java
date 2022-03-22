package com.cs.ui.config.controller.authorization;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.di.config.interactor.authorization.IBulkSavePartnerAuthorization;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class BulkSavePartnerAuthorizationController extends BaseController implements IConfigController {

  @Autowired
  protected IBulkSavePartnerAuthorization  bulkSaveAuthorizationMapping;
  
  @RequestMapping(value = "/bulk/authorizationmappings", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<IdLabelCodeModel> authotizationMappingListModel) throws Exception
  {
    IListModel<IIdLabelCodeModel> authorizationMappinglistSaveModel = new ListModel<>();
    authorizationMappinglistSaveModel.setList(authotizationMappingListModel);
    return createResponse(bulkSaveAuthorizationMapping.execute(authorizationMappinglistSaveModel));
  }
}
