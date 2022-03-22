package com.cs.core.config.taxonomy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;
import com.cs.core.config.klass.AbstractGetSectionInfoService;
import com.cs.core.config.strategy.usecase.taxonomy.IGetSectionInfoForTaxonomyStrategy;

@Service
public class GetSectionInfoForTaxonomyService
    extends AbstractGetSectionInfoService<IGetSectionInfoForTypeRequestModel, IGetSectionInfoModel>
    implements IGetSectionInfoForTaxonomyService {
  
  @Autowired
  protected IGetSectionInfoForTaxonomyStrategy getSectionInfoForTaxonomyStrategy;
  
  @Override
  public IGetSectionInfoModel execute(IGetSectionInfoForTypeRequestModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
  @Override
  protected IGetSectionInfoModel executeGetSectionInfo(IGetSectionInfoForTypeRequestModel model)
      throws Exception
  {
    return getSectionInfoForTaxonomyStrategy.execute(model);
  }
}
