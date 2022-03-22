package com.cs.core.runtime.variant.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.variant.abstrct.AbstractCreateInstanceVariantService;

@Service
public class CreateArticleVariantInstanceService extends AbstractCreateInstanceVariantService<ICreateVariantModel, IGetKlassInstanceModel>
    implements ICreateArticleVariantInstanceService {
  
  @Autowired
  protected Long articleKlassCounter;
  
  @Override
  protected Long getCounter()
  {
    return articleKlassCounter++;
  }
  
  @Override
  protected String getModuleEntityType()
  {
    return CommonConstants.ARTICLE_INSTANCE_MODULE_ENTITY;
  }
  
  @Override
  public BaseType getBaseType()
  {
    return BaseType.ARTICLE;
  }
}
