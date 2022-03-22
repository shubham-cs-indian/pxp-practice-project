package com.cs.core.config.interactor.model.themeconfiguration;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.themeconfiguration.IThemeConfiguration;
import com.cs.core.config.interactor.entity.themeconfiguration.ThemeConfiguration;

public class ThemeConfigurationModel extends ConfigResponseWithAuditLogModel
    implements IThemeConfigurationModel {
  
  private static final long serialVersionUID = 1L;
  IThemeConfiguration       entity;
  
  public ThemeConfigurationModel()
  {
    this.entity = new ThemeConfiguration();
  }
  
  public ThemeConfigurationModel(IThemeConfiguration entity)
  {
    super();
    this.entity = entity;
  }
  
  @Override
  public IThemeConfiguration getEntity()
  {
    return entity;
  }
  
  @Override
  public String getPrimaryLogoId()
  {
    return entity.getPrimaryLogoId();
  }
  
  @Override
  public void setPrimaryLogoId(String primaryLogoId)
  {
    entity.setPrimaryLogoId(primaryLogoId);
  }
  
  @Override
  public String getSecondaryLogoId()
  {
    return entity.getSecondaryLogoId();
  }
  
  @Override
  public void setSecondaryLogoId(String secondaryLogoId)
  {
    entity.setSecondaryLogoId(secondaryLogoId);
  }
  
  @Override
  public String getFaviconId()
  {
    return entity.getFaviconId();
  }
  
  @Override
  public void setFaviconId(String faviconId)
  {
    entity.setFaviconId(faviconId);
  }
  
  @Override
  public String getLogoTitle()
  {
    return entity.getLogoTitle();
  }
  
  @Override
  public void setLogoTitle(String logoTitle)
  {
    entity.setLogoTitle(logoTitle);
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String getHeaderBackgroundColor()
  {
    return entity.getHeaderBackgroundColor();
  }
  
  @Override
  public void setHeaderBackgroundColor(String headerBackgroundColor)
  {
    entity.setHeaderBackgroundColor(headerBackgroundColor);
  }
  
  @Override
  public String getHeaderFontColor()
  {
    return entity.getHeaderFontColor();
  }
  
  @Override
  public void setHeaderFontColor(String headerFontColor)
  {
    entity.setHeaderFontColor(headerFontColor);
  }
  
  @Override
  public String getHeaderIconColor()
  {
    return entity.getHeaderIconColor();
  }
  
  @Override
  public void setHeaderIconColor(String headerIconColor)
  {
    entity.setHeaderIconColor(headerIconColor);
  }
  
  @Override
  public String getDialogBackgroundColor()
  {
    return entity.getDialogBackgroundColor();
  }
  
  @Override
  public void setDialogBackgroundColor(String dialogBackgroundColor)
  {
    entity.setDialogBackgroundColor(dialogBackgroundColor);
  }
  
  @Override
  public String getDialogFontColor()
  {
    return entity.getDialogFontColor();
  }
  
  @Override
  public void setDialogFontColor(String dialogFontColor)
  {
    entity.setDialogFontColor(dialogFontColor);
  }
  
  @Override
  public String getLoginScreenTitle()
  {
    return entity.getLoginScreenTitle();
  }
  
  @Override
  public void setLoginScreenTitle(String loginScreenTitle)
  {
    entity.setLoginScreenTitle(loginScreenTitle);
  }
  
  @Override
  public String getLoginScreenBackgroundImageKey()
  {
    return entity.getLoginScreenBackgroundImageKey();
  }
  
  @Override
  public void setLoginScreenBackgroundImageKey(String loginScreenBackgroundImageKey)
  {
    entity.setLoginScreenBackgroundImageKey(loginScreenBackgroundImageKey);
  }
  
  @Override
  public String getLoginScreenBackgroundThumbKey()
  {
    return entity.getLoginScreenBackgroundThumbKey();
  }
  
  @Override
  public void setLoginScreenBackgroundThumbKey(String loginScreenBackgroundThumbKey)
  {
    entity.setLoginScreenBackgroundThumbKey(loginScreenBackgroundThumbKey);
  }
  
  @Override
  public String getLoginScreenBackgroundColor()
  {
    return entity.getLoginScreenBackgroundColor();
  }
  
  @Override
  public void setLoginScreenBackgroundColor(String loginScreenBackgroundColor)
  {
    entity.setLoginScreenBackgroundColor(loginScreenBackgroundColor);
  }
  
  @Override
  public String getLoginScreenFontColor()
  {
    return entity.getLoginScreenFontColor();
  }
  
  @Override
  public void setLoginScreenFontColor(String loginScreenFontColor)
  {
    entity.setLoginScreenFontColor(loginScreenFontColor);
  }

  @Override
  public void setWelcomeMessage(String welcomeMessage)
  {
    entity.setWelcomeMessage(welcomeMessage);
  }

  @Override
  public String getWelcomeMessage()
  {
    return entity.getWelcomeMessage();
  }

  @Override
  public void setGeneralButtonFontColor(String generalButtonFontColor)
  {
    entity.setGeneralButtonFontColor(generalButtonFontColor);
  }

  @Override
  public String getGeneralButtonFontColor()
  {
    return entity.getGeneralButtonFontColor();
  }

  @Override
  public void setGeneralButtonBackgroundColor(String generalButtonBackgroundColor)
  {
    entity.setGeneralButtonBackgroundColor(generalButtonBackgroundColor);
  }

  @Override
  public String getGeneralButtonBackgroundColor()
  {
    return entity.getGeneralButtonBackgroundColor();
  }

  @Override
  public void setGeneralFontColor(String generalFontColor)
  {
    entity.setGeneralFontColor(generalFontColor);
  }

  @Override
  public String getGeneralFontColor()
  {
    return entity.getGeneralFontColor();
  }

  @Override
  public void setGeneralThemeColor(String generalThemeColor)
  {
    entity.setGeneralThemeColor(generalThemeColor);
  }

  @Override
  public String getGeneralThemeColor()
  {
    return entity.getGeneralThemeColor();
  }

  @Override
  public void setGeneralSelectionColor(String generalSelectionColor)
  {
    entity.setGeneralSelectionColor(generalSelectionColor);
  }

  @Override
  public String getGeneralSelectionColor()
  {
    return entity.getGeneralSelectionColor();
  }

  @Override
  public void setGeneralHeaderLogo(String generalHeaderLogo)
  {
    entity.setGeneralHeaderLogo(generalHeaderLogo);
  }

  @Override
  public String getGeneralHeaderLogo()
  {
    return entity.getGeneralHeaderLogo();
  }

  @Override
  public void setLoginScreenButtonBackgroundColor(String loginScreenButtonBackgroundColor)
  {
    entity.setLoginScreenButtonBackgroundColor(loginScreenButtonBackgroundColor);
  }

  @Override
  public String getLoginScreenButtonBackgroundColor()
  {
    return entity.getLoginScreenButtonBackgroundColor();
  }

  @Override
  public void setLoginScreenButtonFontColor(String loginScreenButtonFontColor)
  {
    entity.setLoginScreenButtonFontColor(loginScreenButtonFontColor);
  }

  @Override
  public String getLoginScreenButtonFontColor()
  {
    return entity.getLoginScreenButtonFontColor();
  }
}
