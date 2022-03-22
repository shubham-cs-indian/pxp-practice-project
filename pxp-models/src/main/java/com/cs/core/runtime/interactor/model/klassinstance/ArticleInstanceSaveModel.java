package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.model.configdetails.IAddedStructureModel;
import com.cs.core.runtime.interactor.entity.klassinstance.ArticleInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IArticleInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceStructure;
import com.cs.core.runtime.interactor.model.instance.AbstractContentInstanceSaveModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.util.List;

@JsonTypeInfo(use = Id.NONE)
public class ArticleInstanceSaveModel extends AbstractContentInstanceSaveModel
    implements IArticleInstanceSaveModel {
  
  private static final long                            serialVersionUID      = 1L;
  
  protected List<IKlassInstanceStructure>              addedStructureAttributeMapping;
  
  protected List<String>                               deletedStructureAttributeMapping;
  
  protected List<IModifiedKlassInstanceStructureModel> modifiedStructureAttributeMapping;
  
  protected List<IAddedStructureModel>                 addedStructureChildren;
  
  protected List<String>                               deletedStructureChildren;
  
  protected List<IStructure>                           modifiedStructureChildren;
  
  protected Long                                       iid;
  
  protected boolean                                    triggerAfterSaveEvent = true;
  
  public ArticleInstanceSaveModel()
  {
    this.entity = new ArticleInstance();
  }
  
  public ArticleInstanceSaveModel(IArticleInstance articleInstance)
  {
    this.entity = articleInstance;
  }
  
  @Override
  public String getBranchOf()
  {
    return ((ArticleInstance) entity).getBranchOf();
  }
  
  @Override
  public void setBranchOf(String branchOf)
  {
    ((ArticleInstance) entity).setBranchOf(branchOf);
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
  
  @Override
  public boolean getTriggerEvent()
  {
    return this.triggerAfterSaveEvent;
  }
  
  @Override
  public void setTriggerEvent(boolean triggerEvent)
  {
    this.triggerAfterSaveEvent = triggerEvent;
  }
  
  @Override
  public Long getIid()
  {
    return iid;
  }
  
  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
}
