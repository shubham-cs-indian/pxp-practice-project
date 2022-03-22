package com.cs.core.config.interactor.entity.themeconfiguration;

public class ThemeConfiguration implements IThemeConfiguration {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected Long            versionId;
  protected Long            versionTimestamp;
  protected String          lastModifiedBy;
  protected String          code;
  protected String          faviconId;
  protected String          primaryLogoId;
  protected String          secondaryLogoId;
  protected String          logoTitle;
  protected String          headerBackgroundColor;
  protected String          headerFontColor;
  protected String          headerIconColor;
  protected String          dialogBackgroundColor;
  protected String          dialogFontColor;
  protected String          loginScreenTitle;
  protected String          loginScreenBackgroundImageKey;
  protected String          loginScreenBackgroundThumbKey;
  protected String          loginScreenBackgroundColor;
  protected String          loginScreenFontColor;
  protected String          welcomeMessage;
  protected String          generalButtonFontColor;
  protected String          generalButtonBackgroundColor;
  protected String          generalFontColor;
  protected String          generalThemeColor;
  protected String          generalSelectionColor;
  protected String          generalHeaderLogo;
  protected String          loginScreenButtonBackgroundColor;
  protected String          loginScreenButtonFontColor;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getFaviconId()
  {
    return faviconId;
  }
  
  @Override
  public void setFaviconId(String faviconId)
  {
    this.faviconId = faviconId;
  }
  
  @Override
  public String getPrimaryLogoId()
  {
    return primaryLogoId;
  }
  
  @Override
  public void setPrimaryLogoId(String primaryLogoId)
  {
    this.primaryLogoId = primaryLogoId;
  }
  
  
  @Override
  public String getLogoTitle()
  {
    return logoTitle;
  }
  
  @Override
  public void setLogoTitle(String logoTitle)
  {
    this.logoTitle = logoTitle;
  }
  
  @Override
  public String getHeaderBackgroundColor()
  {
    return headerBackgroundColor;
  }
  
  @Override
  public void setHeaderBackgroundColor(String headerBackgroundColor)
  {
    this.headerBackgroundColor = headerBackgroundColor;
  }
  
  @Override
  public String getHeaderFontColor()
  {
    return headerFontColor;
  }
  
  @Override
  public void setHeaderFontColor(String headerFontColor)
  {
    this.headerFontColor = headerFontColor;
  }
  
  @Override
  public String getHeaderIconColor()
  {
    return headerIconColor;
  }
  
  @Override
  public void setHeaderIconColor(String headerIconColor)
  {
    this.headerIconColor = headerIconColor;
  }
  
  @Override
  public String getDialogBackgroundColor()
  {
    return dialogBackgroundColor;
  }
  
  @Override
  public void setDialogBackgroundColor(String dialogBackgroundColor)
  {
    this.dialogBackgroundColor = dialogBackgroundColor;
  }
  
  @Override
  public String getDialogFontColor()
  {
    return dialogFontColor;
  }
  
  @Override
  public void setDialogFontColor(String dialogFontColor)
  {
    this.dialogFontColor = dialogFontColor;
  }
  
  @Override
  public String getLoginScreenTitle()
  {
    return loginScreenTitle;
  }
  
  @Override
  public void setLoginScreenTitle(String loginScreenTitle)
  {
    this.loginScreenTitle = loginScreenTitle;
  }
  
  @Override
  public String getLoginScreenBackgroundImageKey()
  {
    return loginScreenBackgroundImageKey;
  }
  
  @Override
  public void setLoginScreenBackgroundImageKey(String loginScreenBackgroundImageKey)
  {
    this.loginScreenBackgroundImageKey = loginScreenBackgroundImageKey;
  }
  
  @Override
  public String getLoginScreenBackgroundThumbKey()
  {
    return loginScreenBackgroundThumbKey;
  }
  
  @Override
  public void setLoginScreenBackgroundThumbKey(String loginScreenBackgroundThumbKey)
  {
    this.loginScreenBackgroundThumbKey = loginScreenBackgroundThumbKey;
  }
  
  @Override
  public String getLoginScreenBackgroundColor()
  {
    return loginScreenBackgroundColor;
  }
  
  @Override
  public void setLoginScreenBackgroundColor(String loginScreenBackgroundColor)
  {
    this.loginScreenBackgroundColor = loginScreenBackgroundColor;
  }
  
  @Override
  public String getLoginScreenFontColor()
  {
    return loginScreenFontColor;
  }
  
  @Override
  public void setLoginScreenFontColor(String loginScreenFontColor)
  {
    this.loginScreenFontColor = loginScreenFontColor;
  }

  @Override
  public void setWelcomeMessage(String welcomeMessage) {
	 this.welcomeMessage = welcomeMessage;
  }

  @Override
  public String getWelcomeMessage()
  {
    return welcomeMessage;
  }
  
  @Override
  public void setGeneralButtonFontColor(String generalButtonFontColor)
  {
    this.generalButtonFontColor = generalButtonFontColor;
  }
  
  @Override
  public String getGeneralButtonFontColor()
  {
    return generalButtonFontColor;
  }
  
  @Override
  public void setGeneralButtonBackgroundColor(String generalButtonBackgroundColor)
  {
      this.generalButtonBackgroundColor =generalButtonBackgroundColor;
  }
  
  @Override
  public String getGeneralButtonBackgroundColor()
  {
    return generalButtonBackgroundColor;
  }
  
  @Override
  public void setGeneralFontColor(String generalFontColor)
  {
     this.generalFontColor = generalFontColor;
  }
  
  @Override
  public String getGeneralFontColor()
  {
    return generalFontColor;
  }
  
  @Override
  public void setGeneralThemeColor(String generalThemeColor)
  {
    this.generalThemeColor= generalThemeColor;
  }
  
  @Override
  public String getGeneralThemeColor()
  {
    return generalThemeColor;
  }
  
  @Override
  public void setGeneralSelectionColor(String generalSelectionColor)
  {
    this.generalSelectionColor= generalSelectionColor;
  }
  
  @Override
  public String getGeneralSelectionColor()
  {
    return generalSelectionColor;
  }
  
  @Override
  public void setGeneralHeaderLogo(String generalHeaderLogo)
  {
    this.generalHeaderLogo= generalHeaderLogo;
  }
  
  @Override
  public String getGeneralHeaderLogo()
  {
    return generalHeaderLogo;
  }
  
  @Override
  public void setLoginScreenButtonBackgroundColor(String loginScreenButtonBackgroundColor)
  {
    this.loginScreenButtonBackgroundColor= loginScreenButtonBackgroundColor;
  }
  
  @Override
  public String getLoginScreenButtonBackgroundColor()
  {
    return loginScreenButtonBackgroundColor;
  }
  
  @Override
  public void setLoginScreenButtonFontColor(String loginScreenButtonFontColor)
  {
    this.loginScreenButtonFontColor= loginScreenButtonFontColor;
  }
  
  @Override
  public String getLoginScreenButtonFontColor()
  {
    return loginScreenButtonFontColor;
  }

  @Override
  public String getSecondaryLogoId()
  {
    return secondaryLogoId;
  }

  @Override
  public void setSecondaryLogoId(String secondaryLogoId)
  {
    this.secondaryLogoId = secondaryLogoId;
  }
}
