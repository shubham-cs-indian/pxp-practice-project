package com.cs.core.config.interactor.model.rulelist;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IRuleListModel extends IConfigModel, IRuleList, IConfigResponseWithAuditLogModel {
  
}
