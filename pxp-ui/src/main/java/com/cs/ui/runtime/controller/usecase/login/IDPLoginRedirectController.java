
package com.cs.ui.runtime.controller.usecase.login;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cs.core.config.interactor.model.user.GetUserValidateModel;
import com.cs.core.config.interactor.model.user.IGetUserValidateModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

/*
 * @Authour: Abhaypratap Singh
 * @Description: To Redirect specific IDP without seeing our application login page
 */

@Controller
public class IDPLoginRedirectController extends BaseController {
  
  @RequestMapping(value = "/idplogin/{id}", method = RequestMethod.GET)
  public String testGet(@PathVariable String id, HttpServletResponse response) throws Exception
  {
    String idpURL = readConfiguration(id);
    if(idpURL != null) {
      return "redirect:" + idpURL;
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return null;
    }
  }
  
  private String readConfiguration(String id)
      throws JsonParseException, JsonMappingException, IOException
  {
    InputStream stream = IDPLoginRedirectController.class.getClassLoader()
        .getResourceAsStream(Constants.SSO_CONFIGURATION_MAPPING_JSON_FILE_NAME);
    
    Map<String, List<IGetUserValidateModel>> idpConfigurationMap = ObjectMapperUtil.readValue(stream, new TypeReference<Map<String, List<GetUserValidateModel>>>(){});
    List<IGetUserValidateModel> idpDetails = idpConfigurationMap.get("mappings");
    IGetUserValidateModel idpConfiguration = idpDetails.stream().filter(x -> x.getId().equals(id)).findFirst().get();
      
    stream.close();
    if (idpConfiguration != null) {
      return idpConfiguration.getUrl();
    }
    
    return null;
  }
  
}
