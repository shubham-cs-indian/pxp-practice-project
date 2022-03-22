package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IContextKlassModel extends IModel {
  
  public static final String CONTEXT_KLASS_ID = "contextKlassId";
  public static final String ATTRIBUTES       = "attributes";
  public static final String TAGS             = "tags";
  public static final String CONTEXT_ID       = "contextId";
  
  public String getContextKlassId();
  
  public void setContextKlassId(String contextKlassId);
  
  public List<IIdAndCouplingTypeModel> getAttributes();
  
  public void setAttributes(List<IIdAndCouplingTypeModel> attributes);
  
  public List<IIdAndCouplingTypeModel> getTags();
  
  public void setTags(List<IIdAndCouplingTypeModel> tags);
  
  public String getContextId();
  
  public void setContextId(String contextId);
}
