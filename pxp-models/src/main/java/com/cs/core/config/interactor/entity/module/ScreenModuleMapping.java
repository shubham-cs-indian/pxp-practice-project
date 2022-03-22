package com.cs.core.config.interactor.entity.module;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

public class ScreenModuleMapping implements IScreenModuleMapping {
  
  private String             id;
  private String             logo;
  private String             type;
  private ArrayList<IScreen> screens;
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreenModuleMapping#getId()
   */
  @Override
  public String getId()
  {
    return this.id;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreenModuleMapping#setId(java.lang.String)
   */
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreenModuleMapping#getLogo()
   */
  @Override
  public String getLogo()
  {
    return this.logo;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreenModuleMapping#setLogo(java.lang.String)
   */
  @Override
  public void setLogo(String logo)
  {
    this.logo = logo;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreenModuleMapping#getType()
   */
  @Override
  public String getType()
  {
    return this.type;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreenModuleMapping#setType(java.lang.String)
   */
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreenModuleMapping#getScreens()
   */
  @Override
  public ArrayList<IScreen> getScreens()
  {
    return this.screens;
  }
  
  /* (non-Javadoc)
   * @see com.cs.config.interactor.entity.module.IScreenModuleMapping#setScreens(java.util.ArrayList)
   */
  @Override
  @JsonDeserialize(contentAs = Screen.class)
  public void setScreens(ArrayList<IScreen> screens)
  {
    this.screens = screens;
  }
}
