package com.cs.config.strategy.plugin.usecase.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.asset.util.AssetUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class ExportClassList extends AbstractGetClassifierTaxonomyList {
  
  public ExportClassList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportClassList/*" };
  }
  
  @Override
  public String getKlassVertexType()
  {
    return VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS;
  }
  
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.execute(requestMap);
  }

  @Override
  public List<Map<String, Object>> executeInternal(Map<String, Object> request) throws Exception
  {
    String entityType = (String) request.get(ConfigTag.entityType.name());
    String klassVertexType = getVertexLabelByEntityType(entityType);
    
    String query = "select from " + klassVertexType + " where " + CommonConstants.CODE_PROPERTY + " not in "
        + EntityUtil.quoteIt(SystemLevelIds.KLASSES_TO_EXCLUDE_FROM_CONFIG_SCREEN)
        + " order by " + EntityUtil.getLanguageConvertedField(IKlass.LABEL) + " asc";
    
    Iterable<Vertex> klassNodes = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    List<Map<String, Object>> klasses = new ArrayList<Map<String, Object>>();
    for (Vertex klassNode : klassNodes) {
      Map<String, Object> klass = getKlassEntityMap(klassNode);
      
      if (klassVertexType.equals(VertexLabelConstants.ENTITY_TYPE_ASSET)) {
        AssetUtils.addAssetExtensionConfigurationDetails(klassNode, klass, ConfigTag.extensionConfiguration.name());
      }
      
      if (!klass.isEmpty())
        klasses.add(klass);
    }
    return klasses;
  }
  
  protected void prepareRelationship(Map<String, Object> relationship)
  {
    relationship.put(ConfigTag.tab.toString(), relationship.remove(IRelationship.TAB_ID));
    super.prepareRelationship(relationship);
  }
}
