package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SaveKlassInstanceStrategyListModel implements ISaveKlassInstanceStrategyListModel {
  
  protected Map<String, Object>                                            variantAndBranchListMap;
  protected List<IModel>                                                   articleKlassInstanceStrategyListModel;
  protected Boolean                                                        isRollback;
  protected List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> contentRelationships;
  protected Collection<? extends IRoleModel>                               roles;
  protected String                                                         loginUserId;
  
  @Override
  public Boolean getIsRollback()
  {
    return isRollback;
  }
  
  @Override
  public void setIsRollback(Boolean isRollback)
  {
    this.isRollback = isRollback;
  }
  
  @Override
  public List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> getContentRelationships()
  {
    return contentRelationships;
  }
  
  @Override
  public void setContentRelationships(
      List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> contentRelationships)
  {
    this.contentRelationships = contentRelationships;
  }
  
  @Override
  public List<IModel> getArticleKlassInstanceStrategyListModel()
  {
    return articleKlassInstanceStrategyListModel;
  }
  
  @Override
  public void setArticleKlassInstanceStrategyListModel(
      List<IModel> articleKlassInstanceStrategyListModel)
  {
    this.articleKlassInstanceStrategyListModel = articleKlassInstanceStrategyListModel;
  }
  
  @Override
  public Map<String, Object> getVariantAndBranchListMap()
  {
    return variantAndBranchListMap;
  }
  
  @Override
  public void setVariantAndBranchListMap(Map<String, Object> variantAndBranchListMap)
  {
    this.variantAndBranchListMap = variantAndBranchListMap;
  }
  
  @Override
  public Collection<? extends IRoleModel> getRoles()
  {
    return roles;
  }
  
  @Override
  public void setRoles(Collection<? extends IRoleModel> roles)
  {
    this.roles = roles;
  }
}
