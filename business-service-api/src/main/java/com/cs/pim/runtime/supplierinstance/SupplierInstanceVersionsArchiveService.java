package com.cs.pim.runtime.supplierinstance;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.klassinstance.AbstractDeleteArticleInstanceVersionService;

@Service
public class SupplierInstanceVersionsArchiveService extends
  AbstractDeleteArticleInstanceVersionService<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
    implements ISupplierInstanceVersionsArchiveService {
  
}

