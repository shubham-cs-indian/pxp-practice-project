package com.cs.ui.config.controller.usecase.mapping;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.ConfigCloneEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.di.config.interactor.mappings.ICloneMappings;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class CloneMappingController extends BaseController implements IConfigController  {
  
  @Autowired
  protected ICloneMappings cloneMappings;
  
  @RequestMapping(value = "/mappings/clone", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<ConfigCloneEntityInformationModel> mappingModel) throws Exception
  {
    IListModel<IConfigCloneEntityInformationModel> mappingsListModel = new ListModel<>();
    mappingsListModel.setList(mappingModel);
    return createResponse(cloneMappings.execute(mappingsListModel));
  } 
  
}
