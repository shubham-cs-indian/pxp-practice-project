package com.cs.core.config.interactor.model.variantconfiguration;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.variantconfiguration.IVariantConfiguration;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface IVariantConfigurationModel
    extends IConfigModel, IVariantConfiguration, IConfigResponseWithAuditLogModel {
  
}
