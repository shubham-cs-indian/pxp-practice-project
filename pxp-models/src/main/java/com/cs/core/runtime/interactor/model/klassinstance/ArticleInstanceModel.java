package com.cs.core.runtime.interactor.model.klassinstance;


import com.cs.core.runtime.interactor.entity.klassinstance.ArticleInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IArticleInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractContentInstanceModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.JsonProcessingException;

@JsonTypeInfo(use = Id.NONE)
public class ArticleInstanceModel extends AbstractContentInstanceModel
    implements IArticleInstanceModel {
  
  private static final long serialVersionUID = 1L;
  
  public ArticleInstanceModel()
  {
    this.entity = new ArticleInstance();
  }
  
  public ArticleInstanceModel(IArticleInstance articleInstance)
  {
    this.entity = articleInstance;
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public Boolean getIsMerged()
  {
    return ((IArticleInstance) entity).getIsMerged();
  }
  
  @Override
  public void setIsMerged(Boolean isMerged)
  {
    ((IArticleInstance) entity).setIsMerged(isMerged);
  }
}
