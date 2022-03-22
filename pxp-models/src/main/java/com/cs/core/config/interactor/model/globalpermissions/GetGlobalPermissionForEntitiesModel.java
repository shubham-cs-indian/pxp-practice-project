package com.cs.core.config.interactor.model.globalpermissions;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetGlobalPermissionForEntitiesModel implements IGetGlobalPermissionForEntitiesModel {
  
  private static final long                         serialVersionUID = 1L;
  
  protected List<IIdLabelWithGlobalPermissionModel> articles;
  protected List<IIdLabelWithGlobalPermissionModel> assets;
  protected List<IIdLabelWithGlobalPermissionModel> markets;
  protected List<IIdLabelWithGlobalPermissionModel> suppliers;
  protected List<IIdLabelWithGlobalPermissionModel> textAssets;
  protected List<IIdLabelWithGlobalPermissionModel> taxonomies;
  protected List<IIdLabelWithGlobalPermissionModel> tasks;
  
  @Override
  public List<IIdLabelWithGlobalPermissionModel> getArticles()
  {
    return articles;
  }
  
  @JsonDeserialize(contentAs = IdLabelWithGlobalPermissionModel.class)
  @Override
  public void setArticles(List<IIdLabelWithGlobalPermissionModel> articles)
  {
    this.articles = articles;
  }
  
  @Override
  public List<IIdLabelWithGlobalPermissionModel> getAssets()
  {
    return assets;
  }
  
  @JsonDeserialize(contentAs = IdLabelWithGlobalPermissionModel.class)
  @Override
  public void setAssets(List<IIdLabelWithGlobalPermissionModel> assets)
  {
    this.assets = assets;
  }
  
  @Override
  public List<IIdLabelWithGlobalPermissionModel> getMarkets()
  {
    return markets;
  }
  
  @JsonDeserialize(contentAs = IdLabelWithGlobalPermissionModel.class)
  @Override
  public void setMarkets(List<IIdLabelWithGlobalPermissionModel> markets)
  {
    this.markets = markets;
  }
  
  @Override
  public List<IIdLabelWithGlobalPermissionModel> getSuppliers()
  {
    return suppliers;
  }
  
  @JsonDeserialize(contentAs = IdLabelWithGlobalPermissionModel.class)
  @Override
  public void setSuppliers(List<IIdLabelWithGlobalPermissionModel> suppliers)
  {
    this.suppliers = suppliers;
  }
  
  @Override
  public List<IIdLabelWithGlobalPermissionModel> getTextAssets()
  {
    return textAssets;
  }
  
  @JsonDeserialize(contentAs = IdLabelWithGlobalPermissionModel.class)
  @Override
  public void setTextAssets(List<IIdLabelWithGlobalPermissionModel> textAssets)
  {
    this.textAssets = textAssets;
  }
  
  @Override
  public List<IIdLabelWithGlobalPermissionModel> getTaxonomies()
  {
    return taxonomies;
  }
  
  @JsonDeserialize(contentAs = IdLabelWithGlobalPermissionModel.class)
  @Override
  public void setTaxonomies(List<IIdLabelWithGlobalPermissionModel> taxonomies)
  {
    this.taxonomies = taxonomies;
  }
  
  @Override
  public List<IIdLabelWithGlobalPermissionModel> getTasks()
  {
    return tasks;
  }
  
  @JsonDeserialize(contentAs = IdLabelWithGlobalPermissionModel.class)
  @Override
  public void setTasks(List<IIdLabelWithGlobalPermissionModel> tasks)
  {
    this.tasks = tasks;
  }
}
