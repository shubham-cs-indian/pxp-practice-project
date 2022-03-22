package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.supplierinstance.ISaveSupplierInstanceRelationshipsService;

@Service("saveSupplierInstanceRelationships")
public class SaveSupplierInstanceRelationships
    extends AbstractRuntimeInteractor<ISaveRelationshipInstanceModel, IGetKlassInstanceModel>
    implements ISaveSupplierInstanceRelationships {
  
  @Autowired
  protected ISaveSupplierInstanceRelationshipsService saveSupplierInstanceRelationshipsService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISaveRelationshipInstanceModel klassInstancesModel) throws Exception
  {
    
    return saveSupplierInstanceRelationshipsService.execute(klassInstancesModel);
  }
}
