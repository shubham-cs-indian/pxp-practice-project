package com.cs.config.strategy.plugin.usecase.export;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetAttributionTaxonomyModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;


public class ExportTaxonomyList extends AbstractGetClassifierTaxonomyList {
  
  public ExportTaxonomyList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportTaxonomyList/*" };
  }
  
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return super.execute(requestMap);
  }
  
  @Override
  public String getKlassVertexType()
  {
    return VertexLabelConstants.ATTRIBUTION_TAXONOMY;
  }
  
  @Override
  public List<Map<String, Object>> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> klasslist = new ArrayList<Map<String, Object>>();
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(
            "select from " + getKlassVertexType() + " where outE('Child_Of').size() = 0 order by "
                + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    for (Vertex klassNode : vertices) {
      //Skip Attribution Taxonomy which is tag and not taxonomy
      if (BooleanUtils.isTrue(klassNode.getProperty(IGetAttributionTaxonomyModel.IS_TAG))
          && BooleanUtils.isFalse(klassNode.getProperty(IGetAttributionTaxonomyModel.IS_TAXONOMY)))
        continue;

      Map<String, Object> taxonomyMap = new HashMap<>();
      taxonomyMap = getKlassEntityMap(klassNode);
      if (!taxonomyMap.isEmpty()) {
        fillTagLevels(klassNode, taxonomyMap);
        //top parent code is -1
        taxonomyMap.put(ConfigTag.parentCode.name(), CommonConstants.STANDARD_PARENT_CODE);
        klasslist.add(taxonomyMap);
        getAllChildren(klassNode, klasslist, 0);
      }
    }
    
    return klasslist;
  }
}
