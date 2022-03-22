package com.cs.core.runtime.interactor.utils.klassinstance;

import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.usecase.klassinstance.SwitchKlassInstanceType;

public class RuntimeBeanFactory {
  
  public static String getSwitchTypeStrategy(String baseType)
  {
    switch (baseType) {
      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
        return SwitchKlassInstanceType.class.getName();
      /*case Constants.ASSET_INSTANCE_BASE_TYPE:
        return SwitchAssetInstanceType.class.getName();
      case Constants.MARKET_INSTANCE_BASE_TYPE:
        return SwitchMarketInstanceType.class.getName();
      case Constants.TEXTASSET_INSTANCE_BASE_TYPE:
        return SwitchTextAssetInstanceType.class.getName();
      case Constants.SUPPLIER_INSTANCE_BASE_TYPE:
        return SwitchSupplierInstanceType.class.getName();
      case Constants.VIRTUAL_CATALOG_INSTANCE_BASE_TYPE:
        return SwitchVirtualCatalogInstanceType.class.getName();*/
      default:
        return null;
    }
  }
}
