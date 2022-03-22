package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IMigrationResponseModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetDependentAttributesAndTechnicalVariantKlassIds extends AbstractOrientMigration {
  
  public GetDependentAttributesAndTechnicalVariantKlassIds(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetDependentAttributesAndTechnicalVariantKlassIds/*" };
  }
  
  @Override
  public Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    String sqlQuery = "select * from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + " where "
        + IAttribute.IS_TRANSLATABLE + " = true ";
    List<String> dependentAttributeIds = new ArrayList<>();
    Map<String, Object> returnMap = new HashMap<>();
    Iterable<Vertex> employeeVertices = UtilClass.getGraph()
        .command(new OCommandSQL(sqlQuery))
        .execute();
    
    for (Vertex vertex : employeeVertices) {
      String dependentAttributeId = UtilClass.getCodeNew(vertex);
      dependentAttributeIds.add(dependentAttributeId);
    }
    
    List<String> technicalImageTypes = getAllTechnicalImages();
    
    UtilClass.getGraph()
        .commit();
    returnMap.put(IMigrationResponseModel.IDS, dependentAttributeIds);
    returnMap.put(IMigrationResponseModel.TECHNICAL_VARIANT_KLASS_IDS, technicalImageTypes);
    
    return returnMap;
  }
  
  private List<String> getAllTechnicalImages()
  {
    String sqlQuery = "select * from " + VertexLabelConstants.ENTITY_TYPE_ASSET + " where "
        + IKlass.NATURE_TYPE + " = '" + CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE + "'";
    List<String> technicalImageTypes = new ArrayList<>();
    Iterable<Vertex> technicalImageVertices = UtilClass.getGraph()
        .command(new OCommandSQL(sqlQuery))
        .execute();
    for (Vertex technicalImageVertex : technicalImageVertices) {
      String technicalImageTypeId = UtilClass.getCodeNew(technicalImageVertex);
      technicalImageTypes.add(technicalImageTypeId);
    }
    return technicalImageTypes;
  }
}
// { "id": "5",
// "pluginName":"Orient_Migration_Script_20_08_2018",
// "createdOnAsString":"20/08/2018",
// "description":"This migration script sets the value of detectDuplicate =
// false in asset klass
// whose id is asset_asset."
// }
