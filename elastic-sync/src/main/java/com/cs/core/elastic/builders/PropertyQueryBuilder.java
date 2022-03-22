package com.cs.core.elastic.builders;

import com.cs.core.elastic.filters.PropertyFilters;
import com.cs.core.elastic.ibuilders.IQueryBuilder;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IFilterDTO;
import com.cs.core.rdbms.entity.idto.IFilterPropertyDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;


public class PropertyQueryBuilder extends QueryBuilder {

  protected PropertyQueryBuilder(ISearchDTO searchDto, BoolQueryBuilder query)
  {
    super(searchDto, query);
  }

  public IQueryBuilder simpleSearch()
  {
    if (!searchDTO.shouldSimpleSearch()) {
      return this;
    }
    QueryStringQueryBuilder simpleSearchQuery = QueryBuilders.queryStringQuery("*" + searchDTO.getStringToSearch() + "*");

    Map<String, Boolean> searchableAttributes = searchDTO.getSearchableAttribute();
    Boolean xrayEnabled = searchDTO.getXrayEnabled();
    NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery(SearchBuilder.Fields.propertyObjects.name(), simpleSearchQuery, ScoreMode.None);
   
    HighlightBuilder highlightBuilder = null;
    InnerHitBuilder innerHitBuilder = null;
    
    if(xrayEnabled) 
    {
      innerHitBuilder = new InnerHitBuilder();
      innerHitBuilder.setName("simpleSearch");
      highlightBuilder = new HighlightBuilder();
      innerHitBuilder.setHighlightBuilder(highlightBuilder);
      nestedQuery.innerHit(innerHitBuilder);  
    }
    
    for (String attributeId : searchableAttributes.keySet()) {
      Boolean isNumeric = searchableAttributes.get(attributeId);
      String attributePath = getAttributePath(attributeId, isNumeric, false);
      simpleSearchQuery.field(attributePath);
      
      if(xrayEnabled) 
      {
        highlightBuilder.field(attributePath);
      }
    }
    
    query.filter(nestedQuery);
    
    return this;
  }

  public IQueryBuilder advanceSearch()
  {
    Boolean xrayEnabled = searchDTO.getXrayEnabled();
    
    for (IFilterPropertyDTO propertyFilters : searchDTO.getPropertyFilters()) {

      BoolQueryBuilder propertyFilter = new BoolQueryBuilder();

      String propertyCode = propertyFilters.getPropertyCode();
      IPropertyDTO.PropertyType propertyType = propertyFilters.getPropertyType();
      IPropertyDTO.SuperType type = propertyType.getSuperType();
      
      InnerHitBuilder innerHitBuilder = null;
      HighlightBuilder highlightBuilder = null;
      
      if(xrayEnabled)
      {
        innerHitBuilder = new InnerHitBuilder();
        innerHitBuilder.setName("advancedSearch" + "_" + propertyCode );
        highlightBuilder = new HighlightBuilder();
        innerHitBuilder.setHighlightBuilder(highlightBuilder);
      }
      
      if (type.equals(IPropertyDTO.SuperType.ATTRIBUTE)) {
        Boolean isNumeric = numericTypes.contains(propertyType);
        for (IFilterDTO filter : propertyFilters.getFilters()) {
          String path = getAttributePath(propertyCode, isNumeric, false);
          String exactPath = getAttributePath(propertyCode, isNumeric, true);
          String wildcardPath = getAttributePathForWildCard(propertyCode, isNumeric);
          String innerHitPath = PropertyFilters.instance().filterForAttribute(filter, path, exactPath, wildcardPath,propertyFilter);
          if(xrayEnabled) 
          {
            highlightBuilder.field(innerHitPath);
          }
        }
      }
      else {
        PropertyFilters.instance().filterForTags(propertyFilters, propertyFilter, propertyCode);
        if(xrayEnabled)
        {
          highlightBuilder.field(NestedPathBuilder.getPathForTag(propertyCode));
        }

      }
      
      NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery(SearchBuilder.Fields.propertyObjects.name(), propertyFilter, ScoreMode.None);
      
      if(xrayEnabled) {
        nestedQuery.innerHit(innerHitBuilder);
      }

      query.filter(nestedQuery);   
    }
    return this;
  }

  private String getAttributePath(String attributeId, Boolean isNumeric, Boolean isExactCheck)
  {
    NestedPathBuilder attributePath = NestedPathBuilder.getPathForAttribute();
    List<String> translatableAttributes = searchDTO.getTranslatableAttributes();
    String localeField = translatableAttributes.contains(attributeId) ? searchDTO.getLocaleId() : SearchBuilder.Fields.independent.name();
    attributePath.append(localeField);
    if (!isExactCheck) {
      attributePath.appendAttributeIdForSearch(attributeId, isNumeric);
    }
    else {
      attributePath.appendAttributeIdForExact(attributeId, isNumeric);
    }
    return attributePath.path();
  }

  private String getAttributePathForWildCard(String attributeId, Boolean isNumeric)
  {
    NestedPathBuilder attributePath = NestedPathBuilder.getPathForAttribute();
    List<String> translatableAttributes = searchDTO.getTranslatableAttributes();
    String localeField = translatableAttributes.contains(attributeId) ? searchDTO.getLocaleId() : SearchBuilder.Fields.independent.name();
    attributePath.append(localeField);
    attributePath.appendAttributeIdForWildcard(attributeId, isNumeric);
    return attributePath.path();
  }

  public IQueryBuilder checkIfPropertyExists(IPropertyDTO propertyDTO)
  {
    BoolQueryBuilder propertyFilter = new BoolQueryBuilder();

    String propertyCode = propertyDTO.getPropertyCode();
    IPropertyDTO.PropertyType propertyType = propertyDTO.getPropertyType();
    Boolean isNumeric = numericTypes.contains(propertyType);
    IPropertyDTO.SuperType type = propertyType.getSuperType();

    if (type.equals(IPropertyDTO.SuperType.ATTRIBUTE)) {
      String exactPath = getAttributePath(propertyCode, isNumeric, false);
      propertyFilter.must(PropertyFilters.instance().attributeIsNotEmptyFilter(exactPath));
    }
    else {
      String pathForTag = NestedPathBuilder.getPathForTag(propertyCode);
      propertyFilter.must(PropertyFilters.instance().tagIsNotEmptyFilter(pathForTag));
    }
    query.filter(QueryBuilders.nestedQuery(SearchBuilder.Fields.propertyObjects.name(), propertyFilter, ScoreMode.None));
    return this;
  }
}
