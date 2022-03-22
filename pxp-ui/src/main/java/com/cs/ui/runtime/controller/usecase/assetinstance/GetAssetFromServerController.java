package com.cs.ui.runtime.controller.usecase.assetinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.asset.GetAssetDetailsRequestModel;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.interactor.usecase.asset.IGetAssetFromServer;
import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GetAssetFromServerController extends BaseController {
  
  public static final String    RESPONSE_HEADER_CONTENT_DISPOSITION = "Content-Disposition";
  public static final String    RESPONSE_HEADER_CONTENT_LENGTH      = "Content-Length";
  
  @Autowired
  protected IGetAssetFromServer getAssetFromServer;
  
  @RequestMapping(value = "/asset/{container}/{key}",
      method = RequestMethod.GET /*, produces={"image/png","image/jpg","video/mp4","video/mov","application/pdf"}*/)
  public void getAssetFromServer(@PathVariable String container, @PathVariable String key,
      @RequestParam(required = false) String download, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    String headerRange = request.getHeader(CommonConstants.HEADER_RANGE);
    String headerIfMatch = request.getHeader(CommonConstants.HEADER_IF_MATCH);
    
    Map<String, String> requestHeadersMap = new HashMap<>();
    if (headerRange != null) {
      requestHeadersMap.put(CommonConstants.HEADER_RANGE, headerRange);
    }
    if (headerIfMatch != null) {
      requestHeadersMap.put(CommonConstants.HEADER_IF_MATCH, headerIfMatch);
    }
    
    IGetAssetDetailsRequestModel dataModel = new GetAssetDetailsRequestModel();
    dataModel.setContainer(container);
    dataModel.setAssetKey(key);
    dataModel.setDownload(download);
    dataModel.setRequestHeaders(requestHeadersMap);
    
    IGetAssetDetailsResponseModel fileModel = getAssetFromServer.execute(dataModel);
    InputStream is = fileModel.getInputStream();
    
    BufferedInputStream input = null;
    BufferedOutputStream output = null;
    response.addHeader(RESPONSE_HEADER_CONTENT_DISPOSITION, fileModel.getContentDisposition());
    
    Map<String, String> responseHeaders = fileModel.getResponseHeaders();
    for (String headername : responseHeaders.keySet()) {
      response.addHeader(headername, responseHeaders.get(headername));
    }
    
    if (headerRange != null) {
      response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
    }
    
    try {
      input = new BufferedInputStream(is);
      output = new BufferedOutputStream(response.getOutputStream());
      byte[] buffer = new byte[8192];
      for (int length = 0; (length = input.read(buffer)) > 0;) {
        output.write(buffer, 0, length);
        // output.flush();
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
