package com.cs.core.runtime.variant.textassetinstance;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.runtime.interactor.exception.textassetinstance.TextAssetInstanceDeleteFailedException;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;
import com.cs.core.runtime.variant.AbstractDeleteInstanceVariantsService;

@Service
public class DeleteTextAssetInstanceVariantsService extends
    AbstractDeleteInstanceVariantsService<IDeleteVariantModel, IBulkDeleteVariantsReturnModel> implements IDeleteTextAssetInstanceVariantsService {
  
  @Resource(name = "textAssetException")
  private Properties            textAssetException;
  
  @Override
  protected IBulkDeleteVariantsReturnModel executeInternal(IDeleteVariantModel model) throws Exception
  {
    try {
      return super.executeInternal(model);
    }
    catch (PluginException e) {
      ExceptionUtil.replaceExceptionKeyWithSpecificExceptionKey(textAssetException, e.getExceptionDetails(), e.getDevExceptionDetails());
      throw new TextAssetInstanceDeleteFailedException(e.getExceptionDetails(), e.getDevExceptionDetails());
    }
  }
}
