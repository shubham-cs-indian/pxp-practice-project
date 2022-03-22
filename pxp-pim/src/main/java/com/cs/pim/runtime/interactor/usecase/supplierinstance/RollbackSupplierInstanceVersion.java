package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;
import com.cs.core.runtime.interactor.version.AbstractRollbackInstanceVersion;
import com.cs.pim.runtime.supplierinstance.IRollbackSupplierInstanceVersionService;

@Service
public class RollbackSupplierInstanceVersion extends AbstractRollbackInstanceVersion<IKlassInstanceVersionRollbackModel, IGetKlassInstanceModel>
    implements IRollbackSupplierInstanceVersion {
  
  @Autowired
  protected IRollbackSupplierInstanceVersionService rollbackSupplierInstanceVersionService;
  
  @Override
  public IGetKlassInstanceModel execute(IKlassInstanceVersionRollbackModel dataModel)
      throws Exception
  {
    return rollbackSupplierInstanceVersionService.execute(dataModel);
  }  
}