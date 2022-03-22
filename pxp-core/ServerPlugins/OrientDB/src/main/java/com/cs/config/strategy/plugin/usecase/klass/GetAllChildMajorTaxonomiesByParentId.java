package com.cs.config.strategy.plugin.usecase.klass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetAllChildMajorTaxonomiesByParentId extends AbstractOrientPlugin {
  
  protected List<String> fieldsToFetch = Arrays.asList(ITaxonomy.LABEL, ITaxonomy.ICON, ITaxonomy.CODE, ITaxonomy.BASE_TYPE);
  
  public GetAllChildMajorTaxonomiesByParentId(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllChildMajorTaxonomiesByParentId/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String parentId = (String) requestMap.get(IGetChildMajorTaxonomiesRequestModel.TAXONOMY_ID);
    Long from = Long.valueOf(requestMap.get(IGetChildMajorTaxonomiesRequestModel.FROM).toString());
    Long size = Long.valueOf(requestMap.get(IGetChildMajorTaxonomiesRequestModel.SIZE).toString());
    String sortBy = requestMap.get(IGetChildMajorTaxonomiesRequestModel.SORT_BY).toString();
    String sortOrder = requestMap.get(IGetChildMajorTaxonomiesRequestModel.SORT_ORDER).toString();
    String searchText = requestMap.get(IGetChildMajorTaxonomiesRequestModel.SEARCH_TEXT).toString();
    String searchColumn = requestMap.get(IGetChildMajorTaxonomiesRequestModel.SEARCH_COLUMN).toString();
    List<String> taxonomyTypes = (List<String>) requestMap.get(IGetChildMajorTaxonomiesRequestModel.TAXONOMY_TYPES);
    
    Map<String,Object> configDetails = new HashMap<String, Object>();
    Map<String, Integer> isPermissionFromRoleOrOrganization = new HashMap<String, Integer>();
    isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 0);    
    // 0 none, 1 -role, 2- organization
    
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
      queries.append("outE('Child_Of').size() = 0 and " + ITaxonomy.TAXONOMY_TYPE + " in " + EntityUtil.quoteIt(taxonomyTypes));
      StringBuilder conditionQuery = EntityUtil.getConditionQuery(queries, searchQuery);
      childTaxonomies = UtilClass.getGraph()
          .command(new OCommandSQL("select from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY +
              conditionQuery + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size))
          .execute();
      
      String countQuery = "select count(*) from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY
          + conditionQuery;
      count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
      
    }
    else {
      Vertex parentTaxonomy = null;
      try {
        parentTaxonomy = UtilClass.getVertexByIndexedId(parentId,VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException(e);
      }
      String rid = parentTaxonomy.getId().toString();
      
      String query = null;
      String countQuery = null;
      if(!searchQuery.toString().isEmpty()) {
        query = "select from (traverse in('"+ RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF +"') from " + rid + ") where code != '" + parentId + "' and " + searchQuery + " order by " + sortBy + " "
            + sortOrder + " skip " + from + " limit " + size;
        countQuery = "select count(*) from (traverse in('"+ RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF +"') from " + rid + ") where code != '" + parentId + "' and " + searchQuery;
      } else {
        query = "select from (traverse in('"+ RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF +"') from " + rid + ") where code != '" + parentId + "' order by " + sortBy + " "
            + sortOrder + " skip " + from + " limit " + size;
        countQuery = "select count(*) from (traverse in('"+ RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF +"') from " + rid + ") where code != '" + parentId + "'";
      }
      
      childTaxonomies = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
      
      count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    }
    
    for (Vertex child : childTaxonomies) {
      Map<String, Object> childMap = UtilClass.getMapFromVertex(fieldsToFetch,child);
      childMap.put(IConfigEntityInformationModel.TYPE, child.getProperty(ITaxonomy.TAXONOMY_TYPE));
      TaxonomyUtil.fillParentIdAndConfigDetails(childMap, configDetails, child,
          isPermissionFromRoleOrOrganization, new ArrayList<String>());
      children.add(childMap);
    }
    
    Map<String,Object> returnMap = new HashMap<>();
    returnMap.put(IGetMajorTaxonomiesResponseModel.LIST, children);
    returnMap.put(IGetMajorTaxonomiesResponseModel.CONFIG_DETAILS, configDetails);
    returnMap.put(IGetMajorTaxonomiesResponseModel.COUNT, count);
    return returnMap;
  }
}

