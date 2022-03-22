package com.cs.core.config.interactor.entity.variantconfiguration;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IVariantConfiguration extends IConfigEntity{
	
	  public static final String IS_SELECT_VARIANT        = "isSelectVariant";
	  
	  public void setIsSelectVariant(Boolean isSelectVariant);
	  public Boolean getIsSelectVariant();

}
