package com.cs.core.runtime.interactor.model.context;

import com.cs.core.runtime.interactor.model.transfer.GetDataTransferModel;
import com.cs.core.runtime.strategy.model.couplingtype.IPropertiesIdCouplingTypeMapModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetContextualDataTransferModel extends GetDataTransferModel
    implements IGetContextualDataTransferModel {
  
  private static final long                                serialVersionUID = 1L;
  
  protected String                                         contextKlassId;
  protected IPropertiesIdCouplingTypeMapModel              dataInheritance;
  protected Map<String, IPropertiesIdCouplingTypeMapModel> dataTransfer;
  protected List<String>                                   variantIdsToExcludeForDataTransfer;
  
  @Override
  public IPropertiesIdCouplingTypeMapModel getDataInheritance()
  {
    return dataInheritance;
  }
  
  @Override
  public void setDataInheritance(IPropertiesIdCouplingTypeMapModel dataInheritance)
  {
    this.dataInheritance = dataInheritance;
  }
  
  @Override
  public Map<String, IPropertiesIdCouplingTypeMapModel> getDataTransfer()
  {
    return dataTransfer;
  }
  
  @Override
  public void setDataTransfer(Map<String, IPropertiesIdCouplingTypeMapModel> dataTransfer)
  {
    this.dataTransfer = dataTransfer;
  }
  
  @Override
  public String getContextKlassId()
  {
    return contextKlassId;
  }
  
  @Override
  public void setContextKlassId(String contextKlassId)
  {
    this.contextKlassId = contextKlassId;
  }
  
  @Override
  public List<String> getVariantIdsToExcludeForDataTransfer()
  {
    if (variantIdsToExcludeForDataTransfer == null) {
      variantIdsToExcludeForDataTransfer = new ArrayList<>();
    }
    return variantIdsToExcludeForDataTransfer;
  }
  
  @Override
  public void setvariantIdsToExcludeForDataTransfer(List<String> variantIdsToExcludeForDataTransfer)
  {
    this.variantIdsToExcludeForDataTransfer = variantIdsToExcludeForDataTransfer;
  }
}
