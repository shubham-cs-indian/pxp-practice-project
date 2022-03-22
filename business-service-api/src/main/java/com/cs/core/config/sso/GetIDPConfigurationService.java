package com.cs.core.config.sso;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.sso.GetIDPConfigurationResponseModel;
import com.cs.core.config.interactor.model.sso.IGetIDPConfigurationResponseModel;
import com.cs.core.config.interactor.model.user.GetUserValidateModel;
import com.cs.core.config.interactor.model.user.IGetUserValidateModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class GetIDPConfigurationService extends AbstractGetConfigService<IModel, IGetIDPConfigurationResponseModel>
    implements IGetIDPConfigurationService {
  
  @Override
  public IGetIDPConfigurationResponseModel executeInternal(IModel dataModel) throws Exception
  {
    InputStream stream = GetIDPConfigurationService.class.getClassLoader()
        .getResourceAsStream(Constants.SSO_CONFIGURATION_MAPPING_JSON_FILE_NAME);
    
    Reader reader = new InputStreamReader(stream, StandardCharsets.ISO_8859_1);
    Map<String, List<IGetUserValidateModel>> idpConfigurationMap = ObjectMapperUtil
        .readValue(reader, new TypeReference<Map<String, List<GetUserValidateModel>>>()
        {
        });
    List<IGetUserValidateModel> idpDetails = idpConfigurationMap.get("mappings");
    stream.close();   
    return new GetIDPConfigurationResponseModel(idpDetails);
  }
}
