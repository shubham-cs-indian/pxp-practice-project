package com.cs.core.runtime.variant.articleinstance;

import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.variant.AbstractBulkSaveInstanceVariants;
import com.cs.pim.runtime.articleinstance.IGetArticleVariantInstancesInTableViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkSaveArticleInstanceVariantsService extends
    AbstractBulkSaveInstanceVariants<IBulkSaveInstanceVariantsModel, IBulkSaveKlassInstanceVariantsResponseModel>
    implements IBulkSaveArticleInstanceVariantsService {

  @Autowired
  protected IGetArticleVariantInstancesInTableViewService getArticleVariantInstancesInTableViewService;

  @Override
  public IBulkSaveKlassInstanceVariantsResponseModel execute(
      IBulkSaveInstanceVariantsModel dataModel) throws Exception
  {
    IBulkSaveKlassInstanceVariantsResponseModel response = null;
    try {
      response = super.executeInternal(dataModel);
    }
    catch (KlassNotFoundException e) {
      throw new ArticleKlassNotFoundException();
    }
    return response;
  }
  
  @Override
  protected String getBaseType()
  {
    return Constants.ARTICLE_INSTANCE_BASE_TYPE;
  }

  protected IGetVariantInstancesInTableViewModel executeGetTableView(IGetVariantInstanceInTableViewRequestModel tableViewRequestModel) throws Exception
  {
    return getArticleVariantInstancesInTableViewService.execute(tableViewRequestModel);
  }
}
