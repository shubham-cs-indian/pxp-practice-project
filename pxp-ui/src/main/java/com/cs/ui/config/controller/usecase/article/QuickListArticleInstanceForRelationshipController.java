package com.cs.ui.config.controller.usecase.article;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.relationship.RelationshipInstanceQuickListModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IQuickListArticleInstanceForRelationship;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class QuickListArticleInstanceForRelationshipController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  IQuickListArticleInstanceForRelationship quickListAssetInstanceRelationship;
  
  @RequestMapping(value = "/klassinstance/relationship/quicklist", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody RelationshipInstanceQuickListModel quickListModel)
      throws Exception
  {
    return createResponse(quickListAssetInstanceRelationship.execute(quickListModel));
  }
}
