package com.cs.core.runtime.interactor.model.filter;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IKlassInstanceQuickListModel extends IGetKlassInstanceTreeStrategyModel {
  
  public static final String RELEVANCE_TAG_IDS   = "relevanceTagIds";
  public static final String KLASS_IDS_HAVING_RP = "klassIdsHavingRP";
  public static final String ALLOWED_TYPES       = "allowedTypes";
  public static final String TYPE_KLASS_ID       = "typeKlassId";
  public static final String ENTITIES            = "entities";
  
  public Collection<String> getRelevanceTagIds();
  
  public void setRelevanceTagIds(Collection<String> relevanceTagIds);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIds);
  
  public List<String> getAllowedTypes();
  
  public void setAllowedTypes(List<String> klassIds);
  
  public String getTypeKlassId();
  
  public void setTypeKlassId(String id);
  
  public Set<String> getEntities();
  
  public void setEntities(Set<String> entities);
}
