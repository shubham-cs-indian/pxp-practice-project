package com.cs.ui.config.controller.usecase.user;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.interactor.usecase.user.IBulkCreateUsers;
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
public class BulkCreateUserController extends BaseController implements IConfigController {
  
  @Autowired
  IBulkCreateUsers createUser;
  
  @RequestMapping(value = "/bulkusers", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody ArrayList<UserModel> userModel) throws Exception
  {
    IListModel<IUserModel> listModel = new ListModel<>();
    listModel.setList(userModel);
    return createResponse(createUser.execute(listModel));
  }
}
