package com.cs.core.config.interactor.entity.module;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

public class Screen implements IScreen {
  
  private String             id;
  private String             name;
  private String             title;
  private String             icon;
  private boolean            isVisible;
  private boolean            isDefault;
  private String             type;
  private String             url;
  private ArrayList<IModule> modules;
  private Boolean            canFilter;
  private Boolean            canSort;
  private Boolean            canFilterTaxonomy;
  private Boolean            isBookmarkEnabled;
  private Boolean            isStaicCollectionEnabled;
  private ArrayList<String>  entities;
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getId()
   */
  @Override
  public String getId()
  {
    return this.id;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setId(java.lang.String)
   */
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getName()
   */
  @Override
  public String getName()
  {
    return this.name;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setName(java.lang.String)
   */
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getTitle()
   */
  @Override
  public String getTitle()
  {
    return this.title;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setTitle(java.lang.String)
   */
  @Override
  public void setTitle(String title)
  {
    this.title = title;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getIcon()
   */
  @Override
  public String getIcon()
  {
    return this.icon;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setIcon(java.lang.String)
   */
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getIsVisible()
   */
  @Override
  public boolean getIsVisible()
  {
    return this.isVisible;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setIsVisible(boolean)
   */
  @Override
  public void setIsVisible(boolean isVisible)
  {
    this.isVisible = isVisible;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getIsDefault()
   */
  @Override
  public boolean getIsDefault()
  {
    return this.isDefault;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setIsDefault(boolean)
   */
  @Override
  public void setIsDefault(boolean isDefault)
  {
    this.isDefault = isDefault;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getType()
   */
  @Override
  public String getType()
  {
    return this.type;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setType(java.lang.String)
   */
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getUrl()
   */
  @Override
  public String getUrl()
  {
    return this.url;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setUrl(java.lang.String)
   */
  @Override
  public void setUrl(String url)
  {
    this.url = url;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getModules()
   */
  @Override
  public ArrayList<IModule> getModules()
  {
    return this.modules;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setModules(java.util.ArrayList)
   */
  @Override
  @JsonDeserialize(contentAs = Module.class)
  public void setModules(ArrayList<IModule> modules)
  {
    this.modules = modules;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getCanFilter()
   */
  @Override
  public Boolean getCanFilter()
  {
    return this.canFilter;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setCanFilter(java.lang.Boolean)
   */
  @Override
  public void setCanFilter(Boolean canFilter)
  {
    this.canFilter = canFilter;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getCanSort()
   */
  @Override
  public Boolean getCanSort()
  {
    return this.canSort;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setCanSort(java.lang.Boolean)
   */
  @Override
  public void setCanSort(Boolean canSort)
  {
    this.canSort = canSort;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getCanFilterTaxonomy()
   */
  @Override
  public Boolean getCanFilterTaxonomy()
  {
    return this.canFilterTaxonomy;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setCanFilterTaxonomy(java.lang.Boolean)
   */
  @Override
  public void setCanFilterTaxonomy(Boolean canFilterTaxonomy)
  {
    this.canFilterTaxonomy = canFilterTaxonomy;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getIsBookmarkEnabled()
   */
  @Override
  public Boolean getIsBookmarkEnabled()
  {
    return this.isBookmarkEnabled;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setIsBookmarkEnabled(java.lang.Boolean)
   */
  @Override
  public void setIsBookmarkEnabled(Boolean isBookmarkEnabled)
  {
    this.isBookmarkEnabled = isBookmarkEnabled;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getIsStaicCollectionEnabled()
   */
  @Override
  public Boolean getIsStaicCollectionEnabled()
  {
    return this.isStaicCollectionEnabled;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setIsStaicCollectionEnabled(java.lang.Boolean)
   */
  @Override
  public void setIsStaicCollectionEnabled(Boolean isStaicCollectionEnabled)
  {
    this.isStaicCollectionEnabled = isStaicCollectionEnabled;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#getEntities()
   */
  @Override
  public ArrayList<String> getEntities()
  {
    return this.entities;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreen#setEntities(java.util.ArrayList)
   */
  @Override
  public void setEntities(ArrayList<String> entities)
  {
    this.entities = entities;
  }
}
