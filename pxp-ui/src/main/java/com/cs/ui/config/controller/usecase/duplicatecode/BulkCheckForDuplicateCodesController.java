package com.cs.ui.config.controller.usecase.duplicatecode;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.usecase.duplicatecode.IBulkCheckForDuplicateCodes;
import com.cs.core.config.strategy.usecase.duplicatecode.BulkCheckForDuplicateCodesModel;
import com.cs.core.config.strategy.usecase.duplicatecode.IBulkCheckForDuplicateCodesModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class BulkCheckForDuplicateCodesController extends BaseController implements IConfigController {
  
  @Autowired
  protected IBulkCheckForDuplicateCodes bulkCheckForDuplicateCodes;
  
  @PostMapping(value = "/bulkcheckentitycode")
  public IRESTModel execute(@RequestBody ArrayList<BulkCheckForDuplicateCodesModel> model) throws Exception
  {
    IListModel<IBulkCheckForDuplicateCodesModel> codeCheckModel = new ListModel<>();
    codeCheckModel.setList(model);
    return createResponse(bulkCheckForDuplicateCodes.execute(codeCheckModel));
  }
}
