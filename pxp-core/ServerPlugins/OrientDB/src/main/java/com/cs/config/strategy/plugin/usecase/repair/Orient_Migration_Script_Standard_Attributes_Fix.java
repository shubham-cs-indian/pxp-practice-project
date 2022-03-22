/*
 * (SetStandardAttributesDisabled) : This migration script dated 30-08-2016 is
 * to update flat field attributes to isDisabled true.
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orient_Migration_Script_Standard_Attributes_Fix extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_Standard_Attributes_Fix(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> response = new HashMap<>();
    List<String> attributeIdsToUpdate = new ArrayList<>();
    attributeIdsToUpdate.add("createdbyattribute");
    attributeIdsToUpdate.add("createdonattribute");
    attributeIdsToUpdate.add("lastmodifiedattribute");
    attributeIdsToUpdate.add("lastmodifiedbyattribute");
    
    String[] attributeIds = { "exifattribute", "iptcattribute", "xmpattribute", "otherattribute",
        "descriptionattribute", "addressattribute", "pincodeattribute", "telattribute",
        "firstnameattribute", "lastnameattribute", "supplierattribute", "xmptoolkit", "documentid",
        "creatortool", "metadatadate", "applicationrecordversion", "captionabstract", "objectname",
        "keywords", "thumbnaillength", "yresolution", "modifydate", "xresolution", "compression",
        "thumbnailoffset", "file", "sourcefile", "composite", "exiftool", "creator", "createdate" };
    List<String> attributesList = Arrays.asList(attributeIds);
    String query = "delete Vertex Attribute where code IN [\"exifattribute\", \"iptcattribute\", \"xmpattribute\", \"otherattribute\",\n"
        + "         \"descriptionattribute\", \"addressattribute\", \"pincodeattribute\", \"telattribute\",\n"
        + "         \"firstnameattribute\", \"lastnameattribute\", \"supplierattribute\", \"xmptoolkit\", \"documentid\",\n"
        + "         \"creatortool\", \"metadatadate\", \"applicationrecordversion\", \"captionabstract\", \"objectname\",\n"
        + "         \"keywords\", \"thumbnaillength\", \"yresolution\", \"modifydate\", \"xresolution\", \"compression\",\n"
        + "         \"thumbnailoffset\", \"file\", \"sourcefile\", \"composite\", \"exiftool\", \"creator\", \"createdate\"]";
    UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    Iterable<Vertex> klassProperties = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_KLASS_PROPERTY);
    
    for (Vertex klassPropertyVertex : klassProperties) {
      boolean isStrandedKlassProperty = true;
      Iterable<Edge> sectionElementVertices = klassPropertyVertex.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_PROPERTY);
      for (Edge edge : sectionElementVertices) {
        isStrandedKlassProperty = false;
        break;
      }
      if (isStrandedKlassProperty) {
        UtilClass.getGraph()
            .removeVertex(klassPropertyVertex);
      }
    }
    
    UtilClass.getGraph()
        .commit();
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Standard_Attributes_Fix/*" };
  }
}
