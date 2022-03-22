package com.cs.core.elastic.filters;

import com.cs.core.elastic.builders.NestedPathBuilder;
import com.cs.core.rdbms.entity.idto.IFilterDTO;
import com.cs.core.rdbms.entity.idto.IFilterPropertyDTO;
import org.apache.commons.lang3.Range;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class PropertyFilters {

  private static final PropertyFilters FILTERS = new PropertyFilters();

  private PropertyFilters() {

  }

  public static PropertyFilters instance()
  {
    return FILTERS;
  }

  /**
   *
   * @param filter information of filter to be executed
   * @param path the path for searchFilter
   * @param exactPath the path for exactFilter
   * @param propertyFilter Query to which the filter query needs to be appended.
   * @param innerHitPath 
   * @return Inner-Hit Path.
   */
  public String filterForAttribute(IFilterDTO filter, String path, String exactPath,
      String pathForWildCardQueries, BoolQueryBuilder propertyFilter)
  {
    Object value = filter.getValue();
    String innerHitPath ="";
    
    switch (filter.getType()) {
      case contains:
        propertyFilter.should(QueryBuilders.wildcardQuery(pathForWildCardQueries, "*" + value.toString() + "*"));
        innerHitPath = pathForWildCardQueries;
        break;

      case lt:
        try {
          Float ValueAsNumber = Float.parseFloat(value.toString());
          propertyFilter.should(QueryBuilders.rangeQuery(exactPath).lt(ValueAsNumber));
          innerHitPath = exactPath;
        }
        catch (NumberFormatException ex) {
        }
        break;

      case gt:
        try {
          Float ValueAsNumber = Float.parseFloat(value.toString());
          propertyFilter.should(QueryBuilders.rangeQuery(exactPath).gt(ValueAsNumber));
          innerHitPath = exactPath;
        }
        catch (NumberFormatException ex) {
        }
        break;

      case exact:
        propertyFilter.should(QueryBuilders.matchQuery(exactPath, value.toString()));
        innerHitPath = exactPath;
        break;

      case start:
        propertyFilter.should(QueryBuilders.prefixQuery(pathForWildCardQueries, value.toString()));
        innerHitPath= pathForWildCardQueries;
        break;

      case end:
        propertyFilter.should(QueryBuilders.wildcardQuery(pathForWildCardQueries, "*" + value.toString()));
        innerHitPath = pathForWildCardQueries;
        break;

      case empty:
        BoolQueryBuilder emptyQuery = new BoolQueryBuilder();
        emptyQuery
            .should(QueryBuilders.matchQuery(path, ""))
            .should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(path)));
        propertyFilter.should(emptyQuery);
        innerHitPath = path;
        break;

      case notempty:
        propertyFilter.should(attributeIsNotEmptyFilter(path));
        innerHitPath =path;
        break;
      case range:

        Range range = (Range) value;
        BoolQueryBuilder rangeQuery = new BoolQueryBuilder();
        rangeQuery.must(
            QueryBuilders.rangeQuery(exactPath)
                .includeLower(true)
                .includeUpper(true)
                .from(range.getMinimum())
                .to(range.getMaximum()));
        propertyFilter.should(rangeQuery);
        innerHitPath = exactPath;
        break;
      default:
        break;
    }
    return innerHitPath;
  }

  public BoolQueryBuilder attributeIsNotEmptyFilter(String path)
  {
    BoolQueryBuilder notEmptyQuery = new BoolQueryBuilder();
    notEmptyQuery
        .mustNot(QueryBuilders.matchQuery(path, ""))
        .mustNot(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(path)));
    return notEmptyQuery;
  }

  public BoolQueryBuilder tagIsNotEmptyFilter(String path)
  {
    BoolQueryBuilder notEmptyQuery = new BoolQueryBuilder();
    notEmptyQuery
        .mustNot(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(path)));
    return notEmptyQuery;
  }

  /**
   *
   * @param filters Tag value filter for whole tag property.
   * @param query The final query in which the filter needs to be added.
   * @param tagCode Code of tag on which filter needs to be applied.
   */
  public void filterForTags(IFilterPropertyDTO filters, BoolQueryBuilder query, String tagCode)
  {
    List<String> tagIds = new ArrayList<>();
    for (IFilterDTO filter : filters.getFilters()) {
      tagIds.add(filter.getValue().toString());
    }
    TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(NestedPathBuilder.getPathForTag(tagCode), tagIds);
    query.filter(termsQueryBuilder);
  }
}
