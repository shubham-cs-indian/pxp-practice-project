package com.cs.ui.config.controller.usecase.variantcontext;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.variantcontext.GridEditVariantContextInformationModel;
import com.cs.core.config.interactor.model.variantcontext.IGridEditVariantContextInformationModel;
import com.cs.core.config.interactor.usecase.variantcontext.IBulkSaveVariantContexts;
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
public class BulkSaveVariantContextsController extends BaseController implements IConfigController {
  
  @Autowired
  protected IBulkSaveVariantContexts bulkSaveVariantContexts;
  
  @RequestMapping(value = "/bulksavevariantcontexts", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<GridEditVariantContextInformationModel> model)
      throws Exception
  {
    IListModel<IGridEditVariantContextInformationModel> bulkSaveVariantContextsModel = new ListModel<>();
    bulkSaveVariantContextsModel.setList(model);
    return createResponse(bulkSaveVariantContexts.execute(bulkSaveVariantContextsModel));
  }
}
