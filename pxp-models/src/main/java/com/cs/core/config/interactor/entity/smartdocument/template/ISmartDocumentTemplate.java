package com.cs.core.config.interactor.entity.smartdocument.template;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;

public interface ISmartDocumentTemplate extends IConfigMasterEntity {
  
  public static final String ZIP_TEMPLATE_ID = "zipTemplateId";
  
  public String getZipTemplateId();
  
  public void setZipTemplateId(String zipTemplateId);
}
