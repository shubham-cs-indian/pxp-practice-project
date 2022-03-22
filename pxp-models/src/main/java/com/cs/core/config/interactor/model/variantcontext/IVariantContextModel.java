package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface IVariantContextModel extends IVariantContext, IConfigModel {
  
  public static final String DEFAULT_TIMERANGE = "defaultTimeRange";
  
  public IDefaultTimeRange getDefaultTimeRange();
  
  public void setDefaultTimeRange(IDefaultTimeRange defaultTimeRange);
}
