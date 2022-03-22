package com.cs.core.runtime.interactor.model.context;

import com.cs.core.runtime.interactor.model.transfer.IGetDataTransferModel;
import com.cs.core.runtime.strategy.model.couplingtype.IPropertiesIdCouplingTypeMapModel;

import java.util.List;
import java.util.Map;

public interface IGetContextualDataTransferModel extends IGetDataTransferModel {
  
  public static final String DATA_INHERITANCE                         = "dataInheritance";
  public static final String DATA_TRANSFER                            = "dataTransfer";
  public static final String CONTEXT_KLASS_ID                         = "contextKlassId";
  public static final String VARIANT_IDS_TO_EXCLUDE_FOR_DATA_TRANSFER = "variantIdsToExcludeForDataTransfer";
  
  public String getContextKlassId();
  
  public void setContextKlassId(String contextKlassId);
  
  public IPropertiesIdCouplingTypeMapModel getDataInheritance();
  
  public void setDataInheritance(IPropertiesIdCouplingTypeMapModel dataInheritance);
  
  // key : context klass Id of child, value : data To Be pushed From Parent
  public Map<String, IPropertiesIdCouplingTypeMapModel> getDataTransfer();
  
  public void setDataTransfer(Map<String, IPropertiesIdCouplingTypeMapModel> dataTransfer);
  
  public List<String> getVariantIdsToExcludeForDataTransfer();
  
  public void setvariantIdsToExcludeForDataTransfer(
      List<String> variantIdsToExcludeForDataTransfer);
}
