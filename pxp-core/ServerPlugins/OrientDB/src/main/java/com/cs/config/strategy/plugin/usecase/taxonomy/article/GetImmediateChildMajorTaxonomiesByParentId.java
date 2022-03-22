package com.cs.config.strategy.plugin.usecase.taxonomy.article;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.taxonomy.IGetImmediateChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.taxonomy.IGetImmediateMajorTaxonomiesResponseModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetImmediateChildMajorTaxonomiesByParentId extends AbstractOrientPlugin {
  
  protected List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      ITaxonomy.LABEL, ITaxonomy.ICON, ITaxonomy.CODE);
  
  public GetImmediateChildMajorTaxonomiesByParentId(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetImmediateChildMajorTaxonomiesByParentId/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String parentId = (String) requestMap
        .get(IGetImmediateChildMajorTaxonomiesRequestModel.TAXONOMY_ID);
    Long from = Long.valueOf(requestMap.get(IGetImmediateChildMajorTaxonomiesRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IGetImmediateChildMajorTaxonomiesRequestModel.SIZE)
        .toString());
    String sortBy = requestMap.get(IGetImmediateChildMajorTaxonomiesRequestModel.SORT_BY)
        .toString();
    String sortOrder = requestMap.get(IGetImmediateChildMajorTaxonomiesRequestModel.SORT_ORDER)
        .toString();
    String searchText = requestMap.get(IGetImmediateChildMajorTaxonomiesRequestModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap
        .get(IGetImmediateChildMajorTaxonomiesRequestModel.SEARCH_COLUMN)
        .toString();
    List<String> taxonomyTypes = (List<String>) requestMap
        .get(IGetImmediateChildMajorTaxonomiesRequestModel.TAXONOMY_TYPES);
    
    if (taxonomyTypes.isEmpty()) {
      taxonomyTypes.add(CommonConstants.MAJOR_TAXONOMY);
    }
    
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    StringBuilder queries = new StringBuilder();
    Iterable<Vertex> childTaxonomies = null;
    List<Map<String, Object>> children = new ArrayList<>();
    Long count = new Long(0);
    if (parentId.equals("-1")) {
      queries.append("outE('Child_Of').size() = 0 and " + ITaxonomy.TAXONOMY_TYPE + " in "
          + EntityUtil.quoteIt(taxonomyTypes));
      StringBuilder conditionQuery = EntityUtil.getConditionQuery(queries, searchQuery);
      childTaxonomies = UtilClass.getGraph()
          .command(new OCommandSQL(
              "select from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY + conditionQuery
                  + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size))
          .execute();
      
      String countQuery = "select count(*) from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY
          + conditionQuery;
      count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
      
    }
    else {
      Vertex parentTaxonomy = null;
      try {
        parentTaxonomy = UtilClass.getVertexByIndexedId(parentId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException(e);
      }
      String rid = parentTaxonomy.getId()
          .toString();
      
      String query = null;
      String countQuery = null;
      if (!searchQuery.toString()
          .isEmpty()) {
        query = "select expand(in('child_of')[" + searchQuery + "]) from " + rid + " order by "
            + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
        countQuery = "select count(*) from ( select expand(in('child_of')[" + searchQuery
            + "]) from " + rid + ")";
      }
      else {
        query = "select expand(in('child_of')) from " + rid + " order by " + sortBy + " "
            + sortOrder + " skip " + from + " limit " + size;
        countQuery = "select count(*) from ( select expand(in('child_of')) from " + rid + ")";
      }
      
      childTaxonomies = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      
      count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    }
    
    for (Vertex child : childTaxonomies) {
      Map<String, Object> childMap = UtilClass.getMapFromVertex(fieldsToFetch, child);
      childMap.put(IConfigEntityInformationModel.TYPE, child.getProperty(ITaxonomy.TAXONOMY_TYPE));
      children.add(childMap);
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetImmediateMajorTaxonomiesResponseModel.LIST, children);
    returnMap.put(IGetImmediateMajorTaxonomiesResponseModel.COUNT, count);
    return returnMap;
  }
}
