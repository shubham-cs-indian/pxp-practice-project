package com.cs.ui.config.controller.usecase.collectionsstatics;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.collections.MoveCollectionNodeModel;
import com.cs.core.config.interactor.usecase.staticcollection.IMoveStaticCollectionHierarchy;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class MoveStaticCollectionHierarchyController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IMoveStaticCollectionHierarchy moveStaticCollectionHierarchy;
  
  @RequestMapping(value = "/movestaticcollections", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody MoveCollectionNodeModel model) throws Exception
  {
    return createResponse(moveStaticCollectionHierarchy.execute(model));
  }
}
