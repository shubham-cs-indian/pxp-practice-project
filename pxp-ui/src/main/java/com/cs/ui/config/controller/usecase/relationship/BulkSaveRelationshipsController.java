package com.cs.ui.config.controller.usecase.relationship;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.BulkSaveRelationshipsModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsModel;
import com.cs.core.config.interactor.usecase.relationship.IBulkSaveRelationships;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class BulkSaveRelationshipsController extends BaseController implements IConfigController {
  
  @Autowired
  protected IBulkSaveRelationships bulkSaveRelationships;
  
  @RequestMapping(value = "/bulksaverelationships", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<BulkSaveRelationshipsModel> model)
      throws Exception
  {
    IListModel<IBulkSaveRelationshipsModel> dataModel = new ListModel<>();
    dataModel.setList(model);
    return createResponse(bulkSaveRelationships.execute(dataModel));
  }
}
