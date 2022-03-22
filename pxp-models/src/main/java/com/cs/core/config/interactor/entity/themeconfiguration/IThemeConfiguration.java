package com.cs.core.config.interactor.entity.themeconfiguration;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IThemeConfiguration extends IConfigEntity {

  public static final String PRIMARY_LOGO_ID                      = "primaryLogoId";
  public static final String SECONDARY_LOGO_ID                    = "secondaryLogoId";
  public static final String FAVICON_ID                           = "faviconId";
  public static final String LOGO_TITLE                           = "logoTitle";
  public static final String HEADER_BACKGROUND_COLOR              = "headerBackgroundColor";
  public static final String HEADER_FONT_COLOR                    = "headerFontColor";
  public static final String HEADER_ICON_COLOR                    = "headerIconColor";
  public static final String DIALOG_BACKGROUND_COLOR              = "dialogBackgroundColor";
  public static final String DIALOG_FONT_COLOR                    = "dialogFontColor";
  public static final String LOGIN_SCREEN_TITLE                   = "loginScreenTitle";
  public static final String LOGIN_SCREEN_BACKGROUND_IMAGE_KEY    = "loginScreenBackgroundImageKey";
  public static final String LOGIN_SCREEN_BACKGROUND_THUMB_KEY    = "loginScreenBackgroundThumbKey";
  public static final String LOGIN_SCREEN_BACKGROUND_COLOR        = "loginScreenBackgroundColor";
  public static final String LOGIN_SCREEN_FONT_COLOR              = "loginScreenFontColor";
  public static final String LOGIN_SCREEN_BUTTON_BACKGROUND_COLOR = "loginScreenButtonBackgroundColor";
  public static final String LOGIN_SCREEN_BUTTON_FONT_COLOR       = "loginScreenButtonFontColor";
  public static final String GENERAL_BUTTON_FONT_COLOR            = "generalButtonFontColor";
  public static final String GENERAL_BUTTON_BACKGROUND_COLOR      = "generalButtonBackgroundColor";
  public static final String GENERAL_FONT_COLOR                   = "generalFontColor";
  public static final String GENERAL_THEME_COLOR                  = "generalThemeColor";
  public static final String GENERAL_SELECTION_COLOR              = "generalSelectionColor";
  public static final String GENERAL_HEADER_LOGO                  = "generalHeaderLogo";
  public static final String WELCOME_MESSAGE                      = "welcomeMessage";

	public String getPrimaryLogoId();

	public void setPrimaryLogoId(String primaryLogoId);
  
  public String getSecondaryLogoId();
  
  public void setSecondaryLogoId(String secondaryLogoId);

	public String getFaviconId();

	public void setFaviconId(String faviconId);

	public String getLogoTitle();

	public void setLogoTitle(String logoTitle);

	public String getHeaderBackgroundColor();

	public void setHeaderBackgroundColor(String headerBackgroundColor);

	public String getHeaderFontColor();

	public void setHeaderFontColor(String headerFontColor);

	public String getHeaderIconColor();

	public void setHeaderIconColor(String headerIconColor);

	public String getDialogBackgroundColor();

	public void setDialogBackgroundColor(String dialogBackgroundColor);

	public String getDialogFontColor();

	public void setDialogFontColor(String dialogFontColor);

	public String getLoginScreenTitle();

	public void setLoginScreenTitle(String loginScreenTitle);

	public String getLoginScreenBackgroundImageKey();

	public void setLoginScreenBackgroundImageKey(String loginScreenBackgroundImageKey);

	public String getLoginScreenBackgroundThumbKey();

	public void setLoginScreenBackgroundThumbKey(String loginScreenBackgroundThumbKey);

	public String getLoginScreenBackgroundColor();

	public void setLoginScreenBackgroundColor(String loginScreenBackgroundColor);

	public String getLoginScreenFontColor();

	public void setLoginScreenFontColor(String loginScreenFontColor);

	public void setWelcomeMessage(String welcomeMessage);

	public String getWelcomeMessage();

	public void setGeneralButtonFontColor(String generalButtonFontColor);

	public String getGeneralButtonFontColor();

	public void setGeneralButtonBackgroundColor(String generalButtonBackgroundColor);

	public String getGeneralButtonBackgroundColor();

	public void setGeneralFontColor(String generalFontColor);

	public String getGeneralFontColor();

	public void setGeneralThemeColor(String generalThemeColor);

	public String getGeneralThemeColor();

	public void setGeneralSelectionColor(String generalSelectionColor);

	public String getGeneralSelectionColor();

	public void setGeneralHeaderLogo(String generalHeaderLogo);

	public String getGeneralHeaderLogo();

	public void setLoginScreenButtonBackgroundColor(String loginScreenButtonBackgroundColor);

	public String getLoginScreenButtonBackgroundColor();

	public void setLoginScreenButtonFontColor(String loginScreenButtonFontColor);

	public String getLoginScreenButtonFontColor();

}
