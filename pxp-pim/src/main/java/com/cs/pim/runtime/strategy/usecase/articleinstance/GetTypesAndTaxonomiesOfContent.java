package com.cs.pim.runtime.strategy.usecase.articleinstance;


import com.cs.core.runtime.interactor.model.klassinstance.IReferencedTypesAndTaxonomiesModel;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;
import com.cs.core.runtime.klassinstance.IGetTypesAndTaxonomiesOfContentService;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetTypesAndTaxonomiesOfContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTypesAndTaxonomiesOfContent implements IGetTypesAndTaxonomiesOfContent {
  
  @Autowired
  protected IGetTypesAndTaxonomiesOfContentService getTypesAndTaxonomiesOfContentService;

  @Override
  public IReferencedTypesAndTaxonomiesModel execute(IAllowedTypesRequestModel model)
      throws Exception
  {
    return getTypesAndTaxonomiesOfContentService.execute(model);
  }
  
}