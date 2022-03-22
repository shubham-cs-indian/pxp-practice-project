package com.cs.core.config.interactor.entity.viewconfiguration;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IViewConfiguration extends IConfigEntity {

	public static final String IS_LANDING_PAGE_EXPANDED = "isLandingPageExpanded";
	public static final String IS_PRODUCT_INFO_PAGE_EXPANDED = "isProductInfoPageExpanded";

	public Boolean getIsLandingPageExpanded();
	public void setIsLandingPageExpanded(Boolean isLandingPageExpanded);

	public Boolean getIsProductInfoPageExpanded();
	public void setIsProductInfoPageExpanded(Boolean isProductInfoPageExpanded);

}
