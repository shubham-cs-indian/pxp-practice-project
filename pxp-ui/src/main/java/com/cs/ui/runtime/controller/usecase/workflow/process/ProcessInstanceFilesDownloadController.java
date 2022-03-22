package com.cs.ui.runtime.controller.usecase.workflow.process;

import com.cs.core.config.interactor.model.articleimportcomponent.ResponseModelForProcessInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.config.interactor.model.articleimportcomponent.IResponseModelForProcessInstance;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.IProcessInstanceFileModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ProcessInstanceFileModel;
import com.cs.di.runtime.strategy.processinstance.IProcessInstanceFiles;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * ProcessInstanceFilesDownloadController class contain a logic to Download file
 * for any workflow process based on workflow process Instance Id .
 *
 * @author Vedant.gupta
 */
@RestController
@RequestMapping(value = "/runtime")
public class ProcessInstanceFilesDownloadController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  IProcessInstanceFiles processInstanceFilesStrategy;
  
  @RequestMapping(value = "/processInstanceFilesDownload/{processInstanceId}", method = RequestMethod.GET)
  public IRESTModel execute(@PathVariable String processInstanceId) throws Exception
  {
    IResponseModelForProcessInstance fileModel = new ResponseModelForProcessInstance();
    //For supporting API to individually download the exported excel file(PXPFDEV-22142)
    IProcessInstanceFileModel model = new ProcessInstanceFileModel();
    model.setId(processInstanceId);
    model.setdownloadIndividual(false);
    model.setfilePath(null);
    try {
      RDBMSLogger.instance().debug("Starting download...");
      fileModel = processInstanceFilesStrategy.execute(model);
    }
    catch (Exception e) {
      RDBMSLogger.instance().debug("Download failed...");
      RDBMSLogger.instance().exception(e);
    }
    return createResponse(fileModel);
  }
}
