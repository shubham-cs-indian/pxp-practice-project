package com.cs.core.config.interactor.entity.module;

import java.util.ArrayList;

public class Module implements IModule {
  
  private String             id;
  private String             label;
  private String             className;
  private String             title;
  private boolean            isSelected;
  private boolean            isVisible;
  private String             url;
  private ArrayList<String>  entities;
  private String             defaultView;
  private ArrayList<String>  allowedViews;
  private Integer            defaultZoomLevel;
  private ArrayList<Integer> allowedZoomLevels;
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#getId()
   */
  @Override
  public String getId()
  {
    return this.id;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#setId(java.lang.String)
   */
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#getLabel()
   */
  @Override
  public String getLabel()
  {
    return this.label;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#setLabel(java.lang.String)
   */
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#getClassName()
   */
  @Override
  public String getClassName()
  {
    return this.className;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#setClassName(java.lang.String)
   */
  @Override
  public void setClassName(String className)
  {
    this.className = className;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#getTitle()
   */
  @Override
  public String getTitle()
  {
    return this.title;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#setTitle(java.lang.String)
   */
  @Override
  public void setTitle(String title)
  {
    this.title = title;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#getIsSelected()
   */
  @Override
  public boolean getIsSelected()
  {
    return this.isSelected;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#setIsSelected(boolean)
   */
  @Override
  public void setIsSelected(boolean isSelected)
  {
    this.isSelected = isSelected;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#getIsVisible()
   */
  @Override
  public boolean getIsVisible()
  {
    return this.isVisible;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#setIsVisible(boolean)
   */
  @Override
  public void setIsVisible(boolean isVisible)
  {
    this.isVisible = isVisible;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#getUrl()
   */
  @Override
  public String getUrl()
  {
    return this.url;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#setUrl(java.lang.String)
   */
  @Override
  public void setUrl(String url)
  {
    this.url = url;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#getEntities()
   */
  @Override
  public ArrayList<String> getEntities()
  {
    return this.entities;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#setEntities(java.util.ArrayList)
   */
  @Override
  public void setEntities(ArrayList<String> entities)
  {
    this.entities = entities;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#getDefaultView()
   */
  @Override
  public String getDefaultView()
  {
    return this.defaultView;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#setDefaultView(java.lang.String)
   */
  @Override
  public void setDefaultView(String defaultView)
  {
    this.defaultView = defaultView;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#getAllowedViews()
   */
  @Override
  public ArrayList<String> getAllowedViews()
  {
    return this.allowedViews;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#setAllowedViews(java.util.ArrayList)
   */
  @Override
  public void setAllowedViews(ArrayList<String> allowedViews)
  {
    this.allowedViews = allowedViews;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#getDefaultZoomLevel()
   */
  @Override
  public Integer getDefaultZoomLevel()
  {
    return this.defaultZoomLevel;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#setDefaultZoomLevel(java.lang.Integer)
   */
  @Override
  public void setDefaultZoomLevel(Integer defaultZoomLevel)
  {
    this.defaultZoomLevel = defaultZoomLevel;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#getAllowedZoomLevels()
   */
  @Override
  public ArrayList<Integer> getAllowedZoomLevels()
  {
    return this.allowedZoomLevels;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IModule#setAllowedZoomLevels(java.util.ArrayList)
   */
  @Override
  public void setAllowedZoomLevels(ArrayList<Integer> allowedZoomLevels)
  {
    this.allowedZoomLevels = allowedZoomLevels;
  }
}
