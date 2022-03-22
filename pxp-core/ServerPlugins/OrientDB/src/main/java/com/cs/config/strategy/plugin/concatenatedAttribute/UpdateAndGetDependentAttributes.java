package com.cs.config.strategy.plugin.concatenatedAttribute;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IInstanceMigrationRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class UpdateAndGetDependentAttributes extends AbstractOrientPlugin {
  
  public UpdateAndGetDependentAttributes(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    UtilClass.getDatabase()
        .commit();
    
    List<String> dependentAttributeCodes = (List<String>) requestMap
        .get(IInstanceMigrationRequestModel.DEPENDENT_ATTRIBUTE_CODES);
    List<String> list = new ArrayList<>();
    HashMap<String, Object> returnMap = new HashMap<>();
    
    String sqlQuery1 = "select * from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE;
    
    Iterable<Vertex> attributeVertices = UtilClass.getGraph()
        .command(new OCommandSQL(sqlQuery1))
        .execute();
    
    for (Vertex attributeVertex : attributeVertices) {
      
      Boolean isNameAttribute = UtilClass.getCodeNew(attributeVertex)
          .equals(IStandardConfig.StandardProperty.nameattribute.toString());
      Boolean isConcatenatedAttribute = attributeVertex.getProperty(IAttribute.TYPE)
          .equals(Constants.CONCATENATED_ATTRIBUTE_TYPE);
      Boolean isDependentAttribute = dependentAttributeCodes
          .contains(attributeVertex.getProperty(IAttribute.CODE));
      
      if (isNameAttribute || isConcatenatedAttribute || isDependentAttribute) {
        attributeVertex.setProperty(IAttribute.IS_TRANSLATABLE, true);
        list.add(UtilClass.getCodeNew(attributeVertex));
      }
      else {
        attributeVertex.setProperty(IAttribute.IS_TRANSLATABLE, false);
      }
    }
    UtilClass.getGraph()
        .commit();
    returnMap.put(IIdsListParameterModel.IDS, list);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|UpdateAndGetDependentAttributes/*" };
  }
}
