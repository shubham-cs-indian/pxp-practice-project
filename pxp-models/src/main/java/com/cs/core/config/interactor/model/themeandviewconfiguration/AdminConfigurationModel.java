package com.cs.core.config.interactor.model.themeandviewconfiguration;

import com.cs.core.config.interactor.entity.themeconfiguration.IThemeConfiguration;
import com.cs.core.config.interactor.entity.themeconfiguration.ThemeConfiguration;
import com.cs.core.config.interactor.entity.viewconfiguration.IViewConfiguration;
import com.cs.core.config.interactor.entity.viewconfiguration.ViewConfiguration;

public class AdminConfigurationModel implements IAdminConfigurationModel {

	private static final long serialVersionUID = 1L;
	private IThemeConfiguration themeEntity;
	private IViewConfiguration viewEntity;

	public AdminConfigurationModel() {
		this.themeEntity = new ThemeConfiguration();
		this.viewEntity = new ViewConfiguration();
	}

	public AdminConfigurationModel(IThemeConfiguration themeEntity, IViewConfiguration viewEntity) {
		this.themeEntity = themeEntity;
		this.viewEntity = viewEntity;
	}

	@Override
	public IThemeConfiguration getEntity() {
		return themeEntity;
	}

	@Override
	public String getPrimaryLogoId() {
		return themeEntity.getPrimaryLogoId();
	}

	@Override
	public void setPrimaryLogoId(String primaryLogoId) {
		themeEntity.setPrimaryLogoId(primaryLogoId);
	}
	
  @Override
  public String getSecondaryLogoId()
  {
    return themeEntity.getSecondaryLogoId();
  }
  
  @Override
  public void setSecondaryLogoId(String secondaryLogoId)
  {
    themeEntity.setSecondaryLogoId(secondaryLogoId);
  }
  
	@Override
	public String getFaviconId() {
		return themeEntity.getFaviconId();
	}

	@Override
	public void setFaviconId(String faviconId) {
		themeEntity.setFaviconId(faviconId);
	}

	@Override
	public String getLogoTitle() {
		return themeEntity.getLogoTitle();
	}

	@Override
	public void setLogoTitle(String logoTitle) {
		themeEntity.setLogoTitle(logoTitle);
	}

	@Override
	public String getCode() {
		return themeEntity.getCode();
	}

	@Override
	public void setCode(String code) {
		themeEntity.setCode(code);
	}

	@Override
	public String getId() {
		return themeEntity.getId();
	}

	@Override
	public void setId(String id) {
		themeEntity.setId(id);
	}

	@Override
	public Long getVersionId() {
		return themeEntity.getVersionId();
	}

	@Override
	public void setVersionId(Long versionId) {
		themeEntity.setVersionId(versionId);
	}

	@Override
	public Long getVersionTimestamp() {
		return themeEntity.getVersionTimestamp();
	}

	@Override
	public void setVersionTimestamp(Long versionTimestamp) {
		themeEntity.setVersionTimestamp(versionTimestamp);
	}

	@Override
	public String getLastModifiedBy() {
		return themeEntity.getLastModifiedBy();
	}

	@Override
	public void setLastModifiedBy(String lastModifiedBy) {
		themeEntity.setLastModifiedBy(lastModifiedBy);
	}

	@Override
	public String getHeaderBackgroundColor() {
		return themeEntity.getHeaderBackgroundColor();
	}

	@Override
	public void setHeaderBackgroundColor(String headerBackgroundColor) {
		themeEntity.setHeaderBackgroundColor(headerBackgroundColor);
	}

	@Override
	public String getHeaderFontColor() {
		return themeEntity.getHeaderFontColor();
	}

	@Override
	public void setHeaderFontColor(String headerFontColor) {
		themeEntity.setHeaderFontColor(headerFontColor);
	}

	@Override
	public String getHeaderIconColor() {
		return themeEntity.getHeaderIconColor();
	}

	@Override
	public void setHeaderIconColor(String headerIconColor) {
		themeEntity.setHeaderIconColor(headerIconColor);
	}

	@Override
	public String getDialogBackgroundColor() {
		return themeEntity.getDialogBackgroundColor();
	}

	@Override
	public void setDialogBackgroundColor(String dialogBackgroundColor) {
		themeEntity.setDialogBackgroundColor(dialogBackgroundColor);
	}

	@Override
	public String getDialogFontColor() {
		return themeEntity.getDialogFontColor();
	}

	@Override
	public void setDialogFontColor(String dialogFontColor) {
		themeEntity.setDialogFontColor(dialogFontColor);
	}

	@Override
	public String getLoginScreenTitle() {
		return themeEntity.getLoginScreenTitle();
	}

	@Override
	public void setLoginScreenTitle(String loginScreenTitle) {
		themeEntity.setLoginScreenTitle(loginScreenTitle);
	}

	@Override
	public String getLoginScreenBackgroundImageKey() {
		return themeEntity.getLoginScreenBackgroundImageKey();
	}

	@Override
	public void setLoginScreenBackgroundImageKey(String loginScreenBackgroundImageKey) {
		themeEntity.setLoginScreenBackgroundImageKey(loginScreenBackgroundImageKey);
	}

	@Override
	public String getLoginScreenBackgroundThumbKey() {
		return themeEntity.getLoginScreenBackgroundThumbKey();
	}

	@Override
	public void setLoginScreenBackgroundThumbKey(String loginScreenBackgroundThumbKey) {
		themeEntity.setLoginScreenBackgroundThumbKey(loginScreenBackgroundThumbKey);
	}

	@Override
	public String getLoginScreenBackgroundColor() {
		return themeEntity.getLoginScreenBackgroundColor();
	}

	@Override
	public void setLoginScreenBackgroundColor(String loginScreenBackgroundColor) {
		themeEntity.setLoginScreenBackgroundColor(loginScreenBackgroundColor);
	}

	@Override
	public String getLoginScreenFontColor() {
		return themeEntity.getLoginScreenFontColor();
	}

	@Override
	public void setLoginScreenFontColor(String loginScreenFontColor) {
		themeEntity.setLoginScreenFontColor(loginScreenFontColor);
	}

	@Override
	public Boolean getIsLandingPageExpanded() {
		return viewEntity.getIsLandingPageExpanded();
	}

	@Override
	public void setIsLandingPageExpanded(Boolean isLandingPageExpanded) {
		viewEntity.setIsLandingPageExpanded(isLandingPageExpanded);
	}

	@Override
	public Boolean getIsProductInfoPageExpanded() {
		return viewEntity.getIsProductInfoPageExpanded();
	}

	@Override
	public void setIsProductInfoPageExpanded(Boolean isProductInfoPageExpanded) {
		viewEntity.setIsProductInfoPageExpanded(isProductInfoPageExpanded);
	}

  @Override
  public void setWelcomeMessage(String welcomeMessage)
  {
    themeEntity.setWelcomeMessage(welcomeMessage);
  }

  @Override
  public String getWelcomeMessage()
  {
    return themeEntity.getWelcomeMessage();
  }

  @Override
  public void setGeneralButtonFontColor(String generalButtonFontColor)
  {
    themeEntity.setGeneralButtonFontColor(generalButtonFontColor);
  }

  @Override
  public String getGeneralButtonFontColor()
  {
    return themeEntity.getGeneralButtonFontColor();
  }

  @Override
  public void setGeneralButtonBackgroundColor(String generalButtonBackgroundColor)
  {
    themeEntity.setGeneralButtonBackgroundColor(generalButtonBackgroundColor);
  }

  @Override
  public String getGeneralButtonBackgroundColor()
  {
    return themeEntity.getGeneralButtonBackgroundColor();
  }

  @Override
  public void setGeneralFontColor(String generalFontColor)
  {
    themeEntity.setGeneralFontColor(generalFontColor);
  }

  @Override
  public String getGeneralFontColor()
  {
    return themeEntity.getGeneralFontColor();
  }

  @Override
  public void setGeneralThemeColor(String generalThemeColor)
  {
    themeEntity.setGeneralThemeColor(generalThemeColor);
  }

  @Override
  public String getGeneralThemeColor()
  {
    return themeEntity.getGeneralThemeColor();
  }

  @Override
  public void setGeneralSelectionColor(String generalSelectionColor)
  {
    themeEntity.setGeneralSelectionColor(generalSelectionColor);
  }

  @Override
  public String getGeneralSelectionColor()
  {
    return themeEntity.getGeneralSelectionColor();
  }

  @Override
  public void setGeneralHeaderLogo(String generalHeaderLogo)
  {
    themeEntity.setGeneralHeaderLogo(generalHeaderLogo);
  }

  @Override
  public String getGeneralHeaderLogo()
  {
    return themeEntity.getGeneralHeaderLogo();
  }

  @Override
  public void setLoginScreenButtonBackgroundColor(String loginScreenButtonBackgroundColor)
  {
    themeEntity.setLoginScreenButtonBackgroundColor(loginScreenButtonBackgroundColor);
  }

  @Override
  public String getLoginScreenButtonBackgroundColor()
  {
    return themeEntity.getLoginScreenButtonBackgroundColor();
  }

  @Override
  public void setLoginScreenButtonFontColor(String loginScreenButtonFontColor)
  {
    themeEntity.setLoginScreenButtonFontColor(loginScreenButtonFontColor);
  }

  @Override
  public String getLoginScreenButtonFontColor()
  {
    return themeEntity.getLoginScreenButtonFontColor();
  }

}
