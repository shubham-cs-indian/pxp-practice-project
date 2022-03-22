package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.xray.XRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigDetailsForRelationshipPaginationModel extends
    ConfigDetailsForTaxonomyHierarchyModel implements IConfigDetailsForRelationshipPaginationModel {
  
  private static final long                             serialVersionUID = 1L;
  protected IXRayConfigDetailsModel                     xRayConfigDetails;
  protected Map<String, IReferencedSectionElementModel> referencedElements;
  protected List<String>                                side2LinkedVariantKrIds;
  
  @Override
  public IXRayConfigDetailsModel getXRayConfigDetails()
  {
    return xRayConfigDetails;
  }
  
  @JsonDeserialize(as = XRayConfigDetailsModel.class)
  @Override
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails)
  {
    this.xRayConfigDetails = xRayConfigDetails;
  }
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
  
  @Override
  public List<String> getSide2LinkedVariantKrIds()
  {
    if(side2LinkedVariantKrIds == null) {
      side2LinkedVariantKrIds = new ArrayList<>();
    }
    return side2LinkedVariantKrIds;
  }

  @Override
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds)
  {
    this.side2LinkedVariantKrIds = side2LinkedVariantKrIds;
  }
}
