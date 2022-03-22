package com.cs.pim.runtime.targetinstance.market;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.klassinstance.AbstractDeleteArticleInstanceVersionService;

@Component
public class MarketInstanceVersionsArchiveService
    extends AbstractDeleteArticleInstanceVersionService<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel>
    implements IMarketInstanceVersionsArchiveService {
  
}
