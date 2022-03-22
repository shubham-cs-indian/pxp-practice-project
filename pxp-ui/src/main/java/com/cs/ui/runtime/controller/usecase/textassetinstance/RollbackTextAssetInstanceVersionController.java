package com.cs.ui.runtime.controller.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.rollback.KlassInstanceVersionRollbackModel;
import com.cs.pim.runtime.interactor.usecase.textassetinstance.IRollbackTextAssetInstanceVersion;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class RollbackTextAssetInstanceVersionController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IRollbackTextAssetInstanceVersion textAssetInstanceVersionRollback;
  
  @RequestMapping(value = "/textassetinstance/versionrollback/{id}", method = RequestMethod.POST)
  public IRESTModel execute(@PathVariable String id, @RequestBody KlassInstanceVersionRollbackModel model) throws Exception
  {
    model.setKlassInstanceId(id);
    
    return createResponse(textAssetInstanceVersionRollback.execute(model));
  }
}
