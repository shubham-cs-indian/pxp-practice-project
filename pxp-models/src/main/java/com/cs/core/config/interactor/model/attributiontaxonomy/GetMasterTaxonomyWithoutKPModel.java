package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.attributiontaxonomy.MasterTaxonomy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetMasterTaxonomyWithoutKPModel extends ConfigResponseWithAuditLogModel implements IGetMasterTaxonomyWithoutKPModel {
  
  private static final long                      serialVersionUID = 1L;
  protected IMasterTaxonomy                      entity;
  protected IGetMasterTaxonomyConfigDetailsModel configDetails;
  
  @Override
  public IMasterTaxonomy getEntity()
  {
    return entity;
  }
  
  @JsonDeserialize(as = MasterTaxonomy.class)
  @Override
  public void setEntity(IMasterTaxonomy entity)
  {
    this.entity = entity;
  }
  
  @Override
  public IGetMasterTaxonomyConfigDetailsModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = GetMasterTaxonomyConfigDetailsModel.class)
  @Override
  public void setConfigDetails(IGetMasterTaxonomyConfigDetailsModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
