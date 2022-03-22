package com.cs.ui.config.controller.usecase.article;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.relationship.SaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.usecase.articleinstance.ISaveArticleInstanceRelationships;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class SaveArticleInstanceRelationshipController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected ISaveArticleInstanceRelationships saveArticleInstanceRelationships;
  
  @RequestMapping(value = "/klassinstance/relationshipinstances", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveRelationshipInstanceModel klassInstanceModels)
      throws Exception
  {
    return createResponse(saveArticleInstanceRelationships.execute(klassInstanceModels));
  }
}
