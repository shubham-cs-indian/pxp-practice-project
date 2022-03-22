package com.cs.ui.runtime.controller.usecase.workflow.process;

import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.usecase.workflow.processinstance.IGetPXONExportFile;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@Controller
@RequestMapping(value = "/runtime")
public class DownloadPXONExportController extends BaseController implements IRuntimeController {
  
  @Autowired
  IGetPXONExportFile getPXONExportFile;
  
  @RequestMapping(value = "/downloadpxon/", method = RequestMethod.GET)
  public IRESTModel execute(@PathVariable String id, HttpServletResponse response) throws Exception
  {
    return null;
  }
  
  @RequestMapping(value = "/downloadpxon/{id}", method = RequestMethod.GET)
  public void downloadPXONExportFile(@PathVariable String id, HttpServletResponse response) throws Exception
  {
    try {
      RDBMSLogger.instance().debug("Starting download...");
      IGetAssetDetailsResponseModel fileModel = getPXONExportFile.execute(new IdParameterModel(id));
      
      response.setContentType(fileModel.getContentType());
      response.setHeader("Content-Disposition", fileModel.getContentDisposition());
      InputStream inputStream = fileModel.getInputStream();
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

  