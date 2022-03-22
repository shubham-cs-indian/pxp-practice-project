package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;

import java.util.List;

public interface IAssetModel extends IKlassModel, IAsset {
  
  public static final String REFERENCED_TAGS                                      = "referencedTags";
  public static final String REFERENCED_CONTEXTS                                  = "referencedContexts";
  public static final String DATA_RULES_OF_KLASS                                  = "dataRulesOfKlass";
  public static final String TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE = "technicalImageVariantContextWithAutoCreateEnable";
  
  public List<? extends ITag> getReferencedTags();
  
  public void setReferencedTags(List<? extends ITag> referencedTags);
  
  public List<IGetVariantContextModel> getReferencedContexts();
  
  public void setReferencedContexts(List<IGetVariantContextModel> referencedContexts);
  
  public List<IDataRuleModel> getDataRulesOfKlass();
  
  public void setDataRulesOfKlass(List<IDataRuleModel> dataRulesOfKlass);
  
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable();
  
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable);
}
