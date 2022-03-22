package com.cs.ui.config.controller.usecase.attribute;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.attribute.ISaveAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.AbstractSaveAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.usecase.attribute.ISaveAttribute;
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
public class SaveAttributeController extends BaseController implements IConfigController {
  
  @Autowired
  private ISaveAttribute saveAttribute;
  
  @RequestMapping(value = "/attributes", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<AbstractSaveAttributeModel> attributeListModel)
      throws Exception
  {
    IListModel<ISaveAttributeModel> attrubutelistSaveModel = new ListModel<>();
    attrubutelistSaveModel.setList(attributeListModel);
    return createResponse(saveAttribute.execute(attrubutelistSaveModel));
  }
}
