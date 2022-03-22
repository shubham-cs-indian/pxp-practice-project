package com.cs.ui.runtime.controller.usecase.dataIntegration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.interactor.model.articleimportcomponent.IResponseModelForProcessInstance;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.di.runtime.interactor.usecase.dataIntegration.IGetImportedFileFromServer;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
public class GetImportedFileFromServerController extends BaseController {
  
  @Autowired
  IGetImportedFileFromServer getImportedFileFromServer;
  
  @RequestMapping(value = "/getimportedfile/{key}", method = RequestMethod.GET)
  public void getAssetFromServer(@PathVariable String key, HttpServletResponse response)
      throws Exception
  {
    try {
      RDBMSLogger.instance()
          .debug("Starting download...");
      IResponseModelForProcessInstance fileModel = getImportedFileFromServer
          .execute(new IdParameterModel(key));
      response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
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
