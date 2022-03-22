package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ISaveKlassInstanceStrategyListModel extends IModel {
  
  // TODO: Change ArticleKlassInstanceStrategyListModel to generic to support
  // all
  public static final String ARTICLE_KLASS_INSTANCE_STRATEGY_LIST_MODEL = "articleKlassInstanceStrategyListModel";
  public static final String VARIANT_AND_BRANCH_LIST_MAP                = "variantAndBranchListMap";
  public static final String IS_ROLLBACK                                = "isRollback";
  public static final String CONTENT_RELATIONSHIPS                      = "contentRelationships";
  public static final String ROLES                                      = "roles";
  
  public List<IModel> getArticleKlassInstanceStrategyListModel();
  
  public void setArticleKlassInstanceStrategyListModel(List<IModel> listModel);
  
  public Map<String, Object> getVariantAndBranchListMap();
  
  public void setVariantAndBranchListMap(Map<String, Object> variantAndBranchListMap);
  
  public Boolean getIsRollback();
  
  public void setIsRollback(Boolean isRollback);
  
  public List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> getContentRelationships();
  
  public void setContentRelationships(
      List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> contentRelationships);
  
  public Collection<? extends IRoleModel> getRoles();
  
  public void setRoles(Collection<? extends IRoleModel> collection);
}
