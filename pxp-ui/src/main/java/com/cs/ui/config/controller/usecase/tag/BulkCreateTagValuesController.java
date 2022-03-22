package com.cs.ui.config.controller.usecase.tag;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.AbstractTagModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.interactor.usecase.tagtype.IBulkCreateTagValues;
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
public class BulkCreateTagValuesController extends BaseController implements IConfigController {
  
  @Autowired
  IBulkCreateTagValues bulkCreateValuesTag;
  
  @RequestMapping(value = "/tags/bulk", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody ArrayList<AbstractTagModel> dataTransferModel)
      throws Exception
  {
    IListModel<ITagModel> taglistSaveModel = new ListModel<>();
    taglistSaveModel.setList(dataTransferModel);
    return createResponse(bulkCreateValuesTag.execute(taglistSaveModel));
  }
}
