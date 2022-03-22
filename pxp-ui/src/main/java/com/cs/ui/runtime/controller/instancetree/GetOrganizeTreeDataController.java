package com.cs.ui.runtime.controller.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.runtime.interactor.model.instancetree.OrganizeTreeDataRequestModel;
import com.cs.core.runtime.interactor.usecase.instancetree.IGetOrganizeTreeData;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class GetOrganizeTreeDataController extends BaseController {
  
  @Autowired
  protected IGetOrganizeTreeData getOrganizeScreenTreeData;
  
  @RequestMapping(value = "/organizescreentree/get", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody OrganizeTreeDataRequestModel model) throws Exception
  {
    return createResponse(getOrganizeScreenTreeData.execute(model));
  }
  
}
