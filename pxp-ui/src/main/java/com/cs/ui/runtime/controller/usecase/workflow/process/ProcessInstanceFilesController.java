package com.cs.ui.runtime.controller.usecase.workflow.process;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.config.interactor.model.articleimportcomponent.IResponseModelForProcessInstance;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.IProcessInstanceFileModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ProcessInstanceFileModel;
import com.cs.di.runtime.strategy.processinstance.IProcessInstanceFiles;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
@RestController
@RequestMapping(value = "/runtime")
public class ProcessInstanceFilesController extends BaseController
implements IRuntimeController {
  
  @Autowired IProcessInstanceFiles processInstanceFilesStrategy;
  
  @RequestMapping(value = "/processInstanceFiles/{processInstanceId}", method = RequestMethod.GET)
  public void execute(@PathVariable String processInstanceId,
      @RequestParam(required = false) boolean downloadIndividual,
      @RequestParam(required = false) String filePath, HttpServletResponse response)
      throws Exception
  {
    try {
      RDBMSLogger.instance().debug("Starting download...");
      IProcessInstanceFileModel model = new ProcessInstanceFileModel();
      model.setId(processInstanceId);
      model.setdownloadIndividual(downloadIndividual);
      model.setfilePath(filePath);    
      IResponseModelForProcessInstance fileModel = processInstanceFilesStrategy.execute(model);
      response.setContentType("application/octet-stream");
      response.setHeader("Content-Disposition", "attachment; filename=" + fileModel.getFileName());
      InputStream inputStream = new ByteArrayInputStream(fileModel.getFileStream());
      IOUtils.copy(inputStream, response.getOutputStream());
      response.flushBuffer();
      inputStream.close();
    }
    catch (Exception e) {
      RDBMSLogger.instance().debug("Download failed...");
      RDBMSLogger.instance().exception(e);
    }
  }  
}
