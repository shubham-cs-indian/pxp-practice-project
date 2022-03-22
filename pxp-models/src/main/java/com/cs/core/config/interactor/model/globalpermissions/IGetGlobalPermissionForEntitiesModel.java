package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetGlobalPermissionForEntitiesModel extends IModel {
  
  public static final String ARTICLES        = "articles";
  public static final String ASSETS          = "assets";
  public static final String MARKETS         = "markets";
  public static final String SUPPLIERS       = "suppliers";
  public static final String TEXT_ASSETS     = "textAssets";
  public static final String TAXONOMIES      = "taxonomies";
  public static final String TASKS           = "tasks";
  
  public List<IIdLabelWithGlobalPermissionModel> getArticles();
  
  public void setArticles(List<IIdLabelWithGlobalPermissionModel> articles);
  
  public List<IIdLabelWithGlobalPermissionModel> getAssets();
  
  public void setAssets(List<IIdLabelWithGlobalPermissionModel> assets);
  
  public List<IIdLabelWithGlobalPermissionModel> getMarkets();
  
  public void setMarkets(List<IIdLabelWithGlobalPermissionModel> markets);
  
  public List<IIdLabelWithGlobalPermissionModel> getSuppliers();
  
  public void setSuppliers(List<IIdLabelWithGlobalPermissionModel> suppliers);
  
  public List<IIdLabelWithGlobalPermissionModel> getTextAssets();
  
  public void setTextAssets(List<IIdLabelWithGlobalPermissionModel> textAssets);
  
  public List<IIdLabelWithGlobalPermissionModel> getTaxonomies();
  
  public void setTaxonomies(List<IIdLabelWithGlobalPermissionModel> taxonomies);
  
  public List<IIdLabelWithGlobalPermissionModel> getTasks();
  
  public void setTasks(List<IIdLabelWithGlobalPermissionModel> tasks);
}
