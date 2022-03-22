package com.cs.core.config.businessapi.variantcontext;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.variantcontext.ICreateVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.strategy.usecase.variantcontext.ICreateVariantContextStrategy;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.utils.ContextUtil;
import coms.cs.core.config.businessapi.variantcontext.ICreateVariantContextService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateVariantContextService extends AbstractCreateConfigService<ICreateVariantContextModel, IGetVariantContextModel>
  implements ICreateVariantContextService {
  
  @Autowired
  ICreateVariantContextStrategy createVariantContextStrategy;
  
  @Override
  public IGetVariantContextModel executeInternal(ICreateVariantContextModel variantContextModel)
      throws Exception
  {
    
    String code = variantContextModel.getCode();
    if (StringUtils.isEmpty(code)) {
      code = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.CONTEXT.getPrefix());
    }
    
    variantContextModel.setCode(code);
    variantContextModel.setId(code);

    ContextValidations.validateContext(variantContextModel);
    ContextType contextType = ContextUtil.getContextTypeByType(variantContextModel.getType());
    RDBMSUtils.newConfigurationDAO().createContext(variantContextModel.getCode(), contextType);
    return createVariantContextStrategy.execute(variantContextModel);
  }
}
