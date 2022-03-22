package com.cs.core.runtime.variant.articleinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveCreatePermissionForArticle;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.runtime.variant.abstrct.AbstractCreateInstanceVariantForLimitedObjectService;
import com.cs.pim.runtime.articleinstance.IGetArticleVariantInstancesInTableViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateArticleInstanceVariantForLimitedObjectService extends AbstractCreateInstanceVariantForLimitedObjectService<ICreateVariantForLimitedObjectRequestModel, IGetVariantInstancesInTableViewModel>
    implements ICreateArticleInstanceVariantForLimitedObjectService {
  
  @Autowired
  protected Long articleKlassCounter;

  @Autowired
  protected IGetArticleVariantInstancesInTableViewService getArticleVariantInstancesInTableViewService;
  
  @Override
  public IGetVariantInstancesInTableViewModel execute(
      ICreateVariantForLimitedObjectRequestModel dataModel) throws Exception
  {
    try {
      IGetVariantInstancesInTableViewModel response = super.executeInternal(dataModel);
      
      return response;
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForArticle();
    }
    catch (KlassNotFoundException e) {
      throw new ArticleKlassNotFoundException();
    }
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.CREATEARTIKLE;
  }
  
  @Override
  protected Long getCounter()
  {
    return articleKlassCounter++;
  }
  
  @Override
  public BaseType getBaseType()
  {
    return BaseType.ARTICLE;
  }
  
  @Override
  protected String getModuleEntityType()
  {
    return CommonConstants.ARTICLE_INSTANCE_MODULE_ENTITY;
  }
  
  protected String getStringBaseType()
  {
    return Constants.ARTICLE_INSTANCE_BASE_TYPE;
  }

  @Override
  protected IGetVariantInstancesInTableViewModel executeGetTableView(IGetVariantInstanceInTableViewRequestModel tableViewRequestModel) throws Exception
  {
    return getArticleVariantInstancesInTableViewService.execute(tableViewRequestModel);
  }
}
