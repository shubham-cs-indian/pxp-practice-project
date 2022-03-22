package com.cs.ui.runtime.controller.usecase.assetinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.relationship.RelationshipInstanceQuickListModel;
import com.cs.dam.runtime.interactor.usecase.assetinstance.IQuickListAssetInstanceForRelationship;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class QuickListAssetInstanceForRelationshipController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  IQuickListAssetInstanceForRelationship quickListAssetInstanceForRelationship;
  
  @RequestMapping(value = "/assetinstance/relationship/quicklist", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody RelationshipInstanceQuickListModel quickListModel)
      throws Exception
  {
    return createResponse(quickListAssetInstanceForRelationship.execute(quickListModel));
  }
}
