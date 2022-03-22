package com.cs.ui.runtime.controller.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.runtime.goldenrecord.bucket.IGetNewFilterAndSortDataForGoldenRecord;
import com.cs.core.runtime.interactor.model.instancetree.GetNewFilterAndSortDataForCollectionRequestModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetNewFilterAndSortDataForGoldenRecordController extends BaseController {
  
  @Autowired
  protected IGetNewFilterAndSortDataForGoldenRecord getNewFilterAndSortDataForGoldenRecord;
  
  @RequestMapping(value = "/goldenrecord/sortandfilter", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetNewFilterAndSortDataForCollectionRequestModel model) throws Exception
  {
    return createResponse(getNewFilterAndSortDataForGoldenRecord.execute(model));
  }
}
