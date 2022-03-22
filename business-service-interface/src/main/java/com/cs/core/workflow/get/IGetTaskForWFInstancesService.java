package com.cs.core.workflow.get;

import com.cs.core.config.interactor.model.articleimportcomponent.IProcessInstanceModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.di.runtime.model.processinstance.IGetProcessInstanceModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public interface IGetTaskForWFInstancesService extends IRuntimeService<IGetProcessInstanceModel, IProcessInstanceModel> {
  
  public static final String SUCCESS_COUNT = "successCount";
  public static final String TOTAL_COUNT   = "totalCount";
  
  /**
   * @param json string
   * @return count - no of records present in specified json string 
   * @throws IOException
   */
  default public Integer getMessageTypeCount(String json)
  {
    List<?> list = Collections.EMPTY_LIST;
    try {
      ObjectMapper mapper = new ObjectMapper();
      list = mapper.readValue(json,List.class);
    }
    catch (Exception jsonExp) {
      RDBMSLogger.instance().exception(jsonExp);
      return 0;
    }
    return list.isEmpty() ? 0 : list.size();
  }  

}