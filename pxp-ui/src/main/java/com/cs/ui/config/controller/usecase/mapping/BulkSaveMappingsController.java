package com.cs.ui.config.controller.usecase.mapping;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.di.config.interactor.mappings.IBulkSaveMappings;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/config")
public class BulkSaveMappingsController extends BaseController implements IConfigController {
  
  @Autowired
  protected IBulkSaveMappings bulkSaveMappings;
  
  @RequestMapping(value = "/bulksavemappings", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<ConfigEntityInformationModel> model)
      throws Exception
  {
    IListModel<IConfigEntityInformationModel> bulkSaveMappingModel = new ListModel<>();
    bulkSaveMappingModel.setList(model);
    return createResponse(bulkSaveMappings.execute(bulkSaveMappingModel));
  }
}
