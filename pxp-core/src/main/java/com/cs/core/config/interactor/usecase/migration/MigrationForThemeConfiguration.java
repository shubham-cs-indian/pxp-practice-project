package com.cs.core.config.interactor.usecase.migration;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.themeconfiguration.IThemeConfigurationModel;
import com.cs.core.config.interactor.model.themeconfiguration.ThemeConfigurationModel;
import com.cs.core.config.strategy.usecase.migration.IMigrationForThemeConfigurationStrategy;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Service
public class MigrationForThemeConfiguration implements IMigrationForThemeConfiguration {
  
  @Autowired
  IMigrationForThemeConfigurationStrategy migrationForThemeConfigurationStrategy;
  
  @Override
  public IVoidModel execute(IVoidModel dataModel) throws Exception
  {
    InputStream stream = this.getClass().getClassLoader().getResourceAsStream(InitializeDataConstants.THEME_CONFIGURATION);
    IThemeConfigurationModel model = ObjectMapperUtil.readValue(stream, ThemeConfigurationModel.class);
    return migrationForThemeConfigurationStrategy.execute(model);
  }

  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
}
