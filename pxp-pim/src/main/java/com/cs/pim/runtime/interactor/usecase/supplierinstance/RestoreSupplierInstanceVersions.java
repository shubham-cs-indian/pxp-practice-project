package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractRestoreInstanceVersions;
import com.cs.pim.runtime.supplierinstance.IRestoreSupplierInstanceVersionsService;

@Service
public class RestoreSupplierInstanceVersions extends
    AbstractRestoreInstanceVersions<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
    implements IRestoreSupplierInstanceVersions {
  
  @Autowired
  IRestoreSupplierInstanceVersionsService restoreSupplierInstanceVersionsService; 
 
  @Override
  public IMoveKlassInstanceVersionsSuccessModel executeInternal(IMoveKlassInstanceVersionsModel dataModel)
      throws Exception
  { 
    return restoreSupplierInstanceVersionsService.execute(dataModel);    
  }

}
