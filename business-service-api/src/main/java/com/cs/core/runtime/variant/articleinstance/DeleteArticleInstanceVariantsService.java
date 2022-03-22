package com.cs.core.runtime.variant.articleinstance;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.exception.articleinstance.ArticleInstanceDeleteFailedException;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;
import com.cs.core.runtime.variant.AbstractDeleteInstanceVariantsService;

@Service
public class DeleteArticleInstanceVariantsService extends
    AbstractDeleteInstanceVariantsService<IDeleteVariantModel, IBulkDeleteVariantsReturnModel> implements IDeleteArticleInstanceVariantsService {
  
  @Resource(name = "articleException")
  private Properties articleException;
  
  @Override
  protected IBulkDeleteVariantsReturnModel executeInternal(IDeleteVariantModel model) throws Exception
  {
    try {
      IBulkDeleteVariantsReturnModel executeInternal = super.executeInternal(model);
      return executeInternal;
    }
    catch (PluginException e) {
      ExceptionUtil.replaceExceptionKeyWithSpecificExceptionKey(articleException, e.getExceptionDetails(), e.getDevExceptionDetails());
      throw new ArticleInstanceDeleteFailedException(e.getExceptionDetails(), e.getDevExceptionDetails());
    }
  }
}
