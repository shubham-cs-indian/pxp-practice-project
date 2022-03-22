package com.cs.ui.config.controller.usecase.iconlibrary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.iconlibrary.IBulkDeleteIcons;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * This is bulk icon deletion controller.
 * @author jamil.ahmad
 *
 */

@RestController
@RequestMapping(value = "config")
public class BulkDeleteIconsController extends BaseController  implements IConfigController{
  
  @Autowired
  protected IBulkDeleteIcons bulkDeleteIcons;
  
  @RequestMapping(value = "/icons", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody IdsListParameterModel getAllModel) throws Exception
  {
    return createResponse(bulkDeleteIcons.execute(getAllModel));
  }
}
