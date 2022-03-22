package com.cs.core.runtime.variant.marketinstance;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.runtime.interactor.exception.marketinstance.MarketInstanceDeleteFailedException;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;
import com.cs.core.runtime.variant.AbstractDeleteInstanceVariantsService;

@Service
public class DeleteMarketInstanceVariantsService extends AbstractDeleteInstanceVariantsService<IDeleteVariantModel, IBulkDeleteVariantsReturnModel>
    implements IDeleteMarketInstanceVariantsService {
  
  @Resource(name = "marketException")
  private Properties marketException;
  
  @Override
  protected IBulkDeleteVariantsReturnModel executeInternal(IDeleteVariantModel model) throws Exception
  {
    try {
      return super.executeInternal(model);
    }
    catch (PluginException e) {
      ExceptionUtil.replaceExceptionKeyWithSpecificExceptionKey(marketException, e.getExceptionDetails(), e.getDevExceptionDetails());
      throw new MarketInstanceDeleteFailedException(e.getExceptionDetails(), e.getDevExceptionDetails());
    }
  }
}
