package com.cs.core.runtime.interactor.model.context;

import com.cs.core.config.interactor.model.klass.ContextKlassModel;
import com.cs.core.config.interactor.model.klass.IContextKlassModel;
import com.cs.core.config.interactor.model.klass.IContextKlassSavePropertiesToInheritModel;
import com.cs.core.config.interactor.model.klass.IModifiedContextKlassModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class ContextKlassSavePropertiesToInheritModel
    implements IContextKlassSavePropertiesToInheritModel {
  
  private static final long                         serialVersionUID = 1L;
  
  protected String                                  taxonomyId;
  protected String                                  klassId;
  protected Map<String, IModifiedContextKlassModel> modifiedContexts;
  protected Map<String, IContextKlassModel>         addedContexts;
  
  @Override
  public String getTaxonomyId()
  {
    return taxonomyId;
  }
  
  @Override
  public void setTaxonmyId(String taxonomyId)
  {
    this.taxonomyId = taxonomyId;
  }
  
  @Override
  public String getKlassId()
  {
    return klassId;
  }
  
  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }
  
  @Override
  public Map<String, IContextKlassModel> getAddedContexts()
  {
    if (addedContexts == null) {
      addedContexts = new HashMap<>();
    }
    return addedContexts;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContextKlassModel.class)
  public void setAddedContexts(Map<String, IContextKlassModel> addedContexts)
  {
    this.addedContexts = addedContexts;
  }
  
  @Override
  public Map<String, IModifiedContextKlassModel> getModifiedContexts()
  {
    if (modifiedContexts == null) {
      modifiedContexts = new HashMap<>();
    }
    return modifiedContexts;
  }
  
  @Override
  @JsonDeserialize(contentAs = ModifiedContextKlassModel.class)
  public void setModifiedContexts(Map<String, IModifiedContextKlassModel> modifiedContexts)
  {
    this.modifiedContexts = modifiedContexts;
  }
}
