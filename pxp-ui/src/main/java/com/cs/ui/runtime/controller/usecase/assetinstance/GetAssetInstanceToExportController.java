package com.cs.ui.runtime.controller.usecase.assetinstance;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.runtime.interactor.model.assetinstance.AssetInstanceExportRequestModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceExportResponseModel;
import com.cs.core.runtime.interactor.usecase.assetinstance.IGetAssetInstanceToExport;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * This controller exports DAM asset in binary.
 * @author rahul.sehrawat
 *
 */
@RestController
@RequestMapping(value = "api")
public class GetAssetInstanceToExportController extends BaseController {
  
  @Autowired
  protected IGetAssetInstanceToExport getAssetInstanceToExport;
  
  @RequestMapping(value = "/assets/{id}", method = RequestMethod.POST)
  public void getAssetInstanceToExport(@PathVariable String id, @RequestBody AssetInstanceExportRequestModel requestModel,
      HttpServletResponse response) throws Exception
  {
    requestModel.setAssetInstanceId(id);
    IAssetInstanceExportResponseModel assetExportResponseModel = getAssetInstanceToExport.execute(requestModel);
    String errorMessage = assetExportResponseModel.getErrorMessage();
    if (errorMessage != null && !errorMessage.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.addHeader("Error-Message", errorMessage);
    } else {
      response.addHeader("Object-FileName", assetExportResponseModel.getFileName());
    }
    
    BufferedInputStream input = null;
    BufferedOutputStream output = null;
    InputStream assetInputStream = assetExportResponseModel.getInputStream();
    
    try {
      input = new BufferedInputStream(assetInputStream);
      output = new BufferedOutputStream(response.getOutputStream());
      byte[] buffer = new byte[8192];
      for (int length = 0; (length = input.read(buffer)) > 0;) {
        output.write(buffer, 0, length);
      }
    }
    finally {
      if (output != null)
        try {
          output.close();
        }
        catch (IOException logOrIgnore) {
        }
      if (input != null)
        try {
          input.close();
        }
        catch (IOException logOrIgnore) {
        }
    }
  }
}
