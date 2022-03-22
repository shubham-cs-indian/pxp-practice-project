package com.cs.ui.runtime.controller.usecase.articleinstance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.klassinstance.CreateKlassInstanceBulkCloneModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ICreateArticleInstanceBulkClone;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class CreateArticleInstanceBulkCloneController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected ICreateArticleInstanceBulkClone createArticleInstanceBulkClone;
  
  @RequestMapping(value = "/articleinstance/bulkclone", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateKlassInstanceBulkCloneModel createCloneModel) throws Exception
  {
    return createResponse(createArticleInstanceBulkClone.execute(createCloneModel));
  }

}