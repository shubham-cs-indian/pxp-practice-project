package com.cs.ui.config.controller.usecase.template;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.SaveCustomTemplateModel;
import com.cs.core.config.interactor.usecase.template.IBulkSaveTemplate;
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
public class BulkSaveTemplateController extends BaseController implements IConfigController {
  
  @Autowired
  protected IBulkSaveTemplate bulkSaveTemplate;
  
  @RequestMapping(value = "/templates/bulksave", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<SaveCustomTemplateModel> saveTemplateModel)
      throws Exception
  {
    IListModel<ISaveCustomTemplateModel> model = new ListModel<>(saveTemplateModel);
    return createResponse(bulkSaveTemplate.execute(model));
  }
}
