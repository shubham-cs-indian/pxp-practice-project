package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.klassinstance.version.AbstractDeleteArticleInstanceVersion;
import com.cs.pim.runtime.supplierinstance.ISupplierInstanceVersionsArchiveService;

@Service
public class SupplierInstanceVersionsArchive extends
    AbstractDeleteArticleInstanceVersion<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
    implements ISupplierInstanceVersionsArchive {
  
  @Autowired 
  protected ISupplierInstanceVersionsArchiveService supplierInstanceVersionsArchiveService; 
  
  @Override
  public IMoveKlassInstanceVersionsSuccessModel execute(IMoveKlassInstanceVersionsModel dataModel)
      throws Exception
  {
    return supplierInstanceVersionsArchiveService.execute(dataModel);
  }
}
