package com.cs.ui.config.controller.usecase.tabs;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;
import com.cs.core.config.interactor.model.tabs.SaveTabModel;
import com.cs.core.config.interactor.usecase.tab.IBulkSaveTab;
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
public class BulkSaveTabController extends BaseController implements IConfigController {
  
  @Autowired
  protected IBulkSaveTab saveTab;
  
  @RequestMapping(value = "/tabs/bulksave", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<SaveTabModel> saveModel) throws Exception
  {
    IListModel<ISaveTabModel> listModel = new ListModel<>();
    listModel.setList(saveModel);
    return createResponse(saveTab.execute(listModel));
  }
}
