package com.cs.core.config.interactor.usecase.variantcontext;

import coms.cs.core.config.businessapi.variantcontext.ICreateVariantContextService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.variantcontext.ICreateVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.strategy.usecase.variantcontext.ICreateVariantContextStrategy;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.utils.ContextUtil;

@Service
public class CreateVariantContext
    extends AbstractCreateConfigInteractor<ICreateVariantContextModel, IGetVariantContextModel>
    implements ICreateVariantContext {
  
  @Autowired
  protected ICreateVariantContextService createVariantContextStrategy;
  
  @Override
  public IGetVariantContextModel executeInternal(ICreateVariantContextModel variantContextModel) throws Exception
  {
    return createVariantContextStrategy.execute(variantContextModel);
  }
}
