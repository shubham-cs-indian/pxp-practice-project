package com.cs.ui.config.controller.usecase.ssoconfiguration;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.sso.CreateSSOConfigurationModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.config.interactor.usecase.sso.ISaveSSOConfiguration;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class SaveSSOConfigurationController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveSSOConfiguration saveGridSSOConfiguation;
  
  @RequestMapping(value = "/sso", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<CreateSSOConfigurationModel> ssoModel)
      throws Exception
  {
    IListModel<ICreateSSOConfigurationModel> ssolistSaveModel = new ListModel<>();
    ssolistSaveModel.setList(ssoModel);
    return createResponse(saveGridSSOConfiguation.execute(ssolistSaveModel));
  }
  
}
