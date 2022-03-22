package com.cs.core.config.interactor.model.portal;

import com.cs.base.interactor.model.portal.IPortalModel;
import com.cs.base.interactor.model.portal.PortalModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class PortalConfiguration {
  
  @Bean
  public List<IPortalModel> getPortal() throws Exception
  {
    
    InputStream portalStream = this.getClass()
        .getClassLoader()
        .getResourceAsStream("portals.json");
    Map<String, List<IPortalModel>> portalMappings = new HashMap<>();
    portalMappings = ObjectMapperUtil.readValue(portalStream,
        new TypeReference<Map<String, List<PortalModel>>>()
        {
          
        });
    
    List<IPortalModel> portalModels = portalMappings.get(Constants.PORTALS);
    
    return portalModels;
  }
}
