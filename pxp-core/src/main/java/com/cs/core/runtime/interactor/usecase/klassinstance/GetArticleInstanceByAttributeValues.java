package com.cs.core.runtime.interactor.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import org.springframework.stereotype.Component;

@Component
public class GetArticleInstanceByAttributeValues implements IGetInstanceTreeStrategy {
  
  @SuppressWarnings("rawtypes")
  @Override
  public IGetKlassInstanceTreeModel execute(IGetKlassInstanceTreeStrategyModel model)
      throws Exception
  {
    /* List<ArticleInstance> articleInstances = new ArrayList<>();
    List<? extends IPropertyInstanceFilterModel> attributes = model.getAttributes();
    
    IPropertyInstanceFilterModel propertyInstanceFilterModel = attributes.get(0);
    FilterValueMatchModel mandatory = (FilterValueMatchModel) propertyInstanceFilterModel.getMandatory().get(0);
    
    String type = mandatory.getType();
    String value = mandatory.getValue();
    String attributeId = propertyInstanceFilterModel.getId();
    
    if (type.equals("contains")) {
      articleInstances =  PostgreSearchAPIUtil.searchByAttributeValueContains(value, attributeId);
    }
    else if (type.equals("exact")) {
      articleInstances =  PostgreSearchAPIUtil.searchByAttributeValueExact(value, attributeId);
    }
    else if (type.equals("start")) {
      articleInstances =  PostgreSearchAPIUtil.searchByAttributeValueStartsWith(value, attributeId);
    }
    else if (type.equals("end")) {
      articleInstances =  PostgreSearchAPIUtil.searchByAttributeValueEndsWith(value, attributeId);
    }*/
    
    IGetKlassInstanceTreeModel responseModel = new GetKlassInstanceTreeModel();
    // responseModel.setChildren(PostgreInstanceUtil.getIKlassInstanceInformationModels(articleInstances));
    
    return responseModel;
  }
}
