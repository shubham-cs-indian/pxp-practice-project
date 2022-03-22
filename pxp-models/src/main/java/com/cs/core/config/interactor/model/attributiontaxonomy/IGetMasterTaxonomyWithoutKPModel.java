package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetMasterTaxonomyWithoutKPModel extends IModel, IConfigResponseWithAuditLogModel {
  
  public static final String ENTITY         = "entity";
  public static final String CONFIG_DETAILS = "configDetails";
  
  public IMasterTaxonomy getEntity();
  
  public void setEntity(IMasterTaxonomy entity);
  
  public IGetMasterTaxonomyConfigDetailsModel getConfigDetails();
  
  public void setConfigDetails(IGetMasterTaxonomyConfigDetailsModel configDetails);
}
