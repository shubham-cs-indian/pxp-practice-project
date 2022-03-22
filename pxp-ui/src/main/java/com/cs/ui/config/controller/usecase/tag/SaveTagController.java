package com.cs.ui.config.controller.usecase.tag;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ISaveTagModel;
import com.cs.core.config.interactor.model.tag.SaveTagModel;
import com.cs.core.config.interactor.usecase.tag.ISaveTag;
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
public class SaveTagController extends BaseController implements IConfigController {
  
  @Autowired
  ISaveTag saveTag;
  
  @RequestMapping(value = "/tags", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<SaveTagModel> dataTransferModel) throws Exception
  {
    IListModel<ISaveTagModel> taglistSaveModel = new ListModel<>();
    taglistSaveModel.setList(dataTransferModel);
    return createResponse(saveTag.execute(taglistSaveModel));
  }
}
