package com.cs.core.base.interactor.model;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IModuleEntitiesWithUserIdModel extends IModel {
  
  public static String ID                  = "id";
  public static String USER_ID             = "userId";
  public static String KLASS_ID            = "klassId";
  public static String CLICKED_TAXONOMY_ID = "clickedTaxonomyId";
  public static String ALLOWED_ENTITIES    = "allowedEntities";
  
  public String getId();
  public void setId(String id);
  
  public String getUserId();
  public void setUserId(String userId);
  
  public String getKlassId();
  public void setKlassId(String klassId);

  public String getClickedTaxonomyId();
  public void setClickedTaxonomyId(String clickedTaxonomyId);

  public List<String> getAllowedEntities();
  public void setAllowedEntities(List<String> moduleEntities);

}
