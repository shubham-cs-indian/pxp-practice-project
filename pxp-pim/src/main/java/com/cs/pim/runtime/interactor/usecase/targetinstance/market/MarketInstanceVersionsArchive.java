package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.usecase.klassinstance.version.AbstractDeleteArticleInstanceVersion;
import com.cs.pim.runtime.targetinstance.market.IMarketInstanceVersionsArchiveService;

@Service
public class MarketInstanceVersionsArchive
    extends AbstractDeleteArticleInstanceVersion<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
    implements IMarketInstanceVersionsArchive {
  
  @Autowired
  IMarketInstanceVersionsArchiveService marketInstanceVersionsArchiveService;
  
  @Override
  public IMoveKlassInstanceVersionsSuccessModel execute(IMoveKlassInstanceVersionsModel dataModel) throws Exception
  {
    return marketInstanceVersionsArchiveService.execute(dataModel);
  }
  
}
