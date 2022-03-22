package com.cs.core.config.interactor.model.themeconfiguration;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.themeconfiguration.IThemeConfiguration;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface IThemeConfigurationModel
    extends IConfigModel, IThemeConfiguration, IConfigResponseWithAuditLogModel {
  
}
