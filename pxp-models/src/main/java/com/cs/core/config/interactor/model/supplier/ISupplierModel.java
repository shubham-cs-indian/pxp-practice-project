package com.cs.core.config.interactor.model.supplier;

import com.cs.core.config.interactor.entity.supplier.ISupplier;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;

import java.util.List;

public interface ISupplierModel extends IKlassModel, ISupplier {
  
  public static final String REFERENCED_TAGS                                      = "referencedTags";
  public static final String TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE = "technicalImageVariantContextWithAutoCreateEnable";
  
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable();
  
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable);
  
  public List<? extends ITag> getReferencedTags();
  
  public void setReferencedTags(List<? extends ITag> referencedTags);
}
