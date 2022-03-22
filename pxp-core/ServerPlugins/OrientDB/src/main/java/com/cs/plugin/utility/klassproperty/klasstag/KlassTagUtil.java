package com.cs.plugin.utility.klassproperty.klasstag;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.plugin.utility.property.tag.TagDbUtil;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class KlassTagUtil {
  
  /**
   * @author Rohit.Raina
   * @param klassTags
   * @return
   */
  public static Boolean checkIsAllTagValuesAllowed(Iterable<Vertex> klassTags)
  {
    if (!klassTags.iterator()
        .hasNext()) {
      return true;
    }
    for (Vertex klassTag : klassTags) {
      Boolean isAllTagvaluesAllowed = checkIsAllTagValueAllowed(klassTag);
      if (isAllTagvaluesAllowed) {
        return true;
      }
    }
    
    return false;
  }
  
  /**
   * @author Rohit.Raina
   * @param klassTag
   * @return
   */
  public static Boolean checkIsAllTagValueAllowed(Vertex klassTag)
  {
    Iterator<Vertex> allowedTagValues = klassTag
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_KLASS_TAG_VALUE)
        .iterator();
    if (!allowedTagValues.hasNext()) {
      return true;
    }
    return false;
  }
  
  /**
   * @author Rohit.Raina
   * @param tagGroupId
   * @param klassIds
   * @param searchColumn
   * @param searchText
   * @return
   * @throws Exception
   */
  public static List<String> getAllowedTagValueIds(String tagGroupId, List<String> klassIds,
      String searchColumn, String searchText) throws Exception
  {
    Iterable<Vertex> klassTagVertices = KlassTagDbUtil
        .getKlassTagVerticesFromTagAndKlasses(tagGroupId, klassIds);
    List<Vertex> klassTagVerticesList = (StreamSupport.stream(klassTagVertices.spliterator(),
        false)).collect(Collectors.toList());
    
    Boolean isAllTagValuesAllowed = KlassTagUtil.checkIsAllTagValuesAllowed(klassTagVerticesList);
    
    List<String> allowedTagValueIds = new ArrayList<>();
    if (isAllTagValuesAllowed) {
      allowedTagValueIds = TagDbUtil.getTagValueIds(tagGroupId, searchColumn, searchText);
    }
    else {
      
      Set<String> allowedTagValueIdsSet = new HashSet<>();
      for (Vertex klassTagVertexList : klassTagVerticesList) {
        List<String> tagValueIds = KlassTagDbUtil.getAllowedTagValueIds(
            UtilClass.getCodeNew(klassTagVertexList), searchColumn, searchText);
        allowedTagValueIdsSet.addAll(tagValueIds);
      }
      allowedTagValueIds.addAll(allowedTagValueIdsSet);
    }
    
    return allowedTagValueIds;
  }
}
