package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IArticleInstanceWithDiffModel extends IRuntimeModel, IArticleInstanceModel {
  
  public static final String KLASS_INSTANCE_DIFF = "klassInstanceDiff";
  public static final String REFERENCED_ASSETS   = "referencedAssets";
  
  public IKlassInstanceDiffModel getKlassInstanceDiff();
  
  public void setKlassInstanceDiff(IKlassInstanceDiffModel klassInstanceDiffModel);
  
  public Map<String, ? extends IAssetAttributeInstanceInformationModel> getReferencedAssets();
  
  public void setReferencedAssets(
      Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets);
}
