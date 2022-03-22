package com.cs.core.config.interactor.model.configdetails;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetPostConfigDetailsResponseModel extends IModel {
  
  public static final String ATTRIBUTES         = "attributes";
  public static final String TAGS               = "tags";
  public static final String REFERENCED_KLASSES = "referencedKlasses";
  
  public List<IAttribute> getAttributes();
  
  public void setAttributes(List<IAttribute> attributes);
  
  public List<ITag> getTags();
  
  public void setTags(List<ITag> attributes);
  
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses);
}
