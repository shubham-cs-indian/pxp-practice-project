package com.cs.core.elastic.builders;

import com.cs.core.elastic.ibuilders.IAggregator;
import com.cs.core.elastic.ibuilders.IQueryBuilder;
import com.cs.core.elastic.ibuilders.ISearchBuilder;
import com.cs.core.rdbms.entity.idto.ISortDTO;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;

/**
 * The responsibility of this class is to facilitate API commonly required for searching.
 * @author Niraj.Dighe
 */
public class SearchBuilder implements ISearchBuilder {

  private SearchSourceBuilder source;
  private String              localeID;

  public SearchBuilder(String localeID)
  {
    this.localeID = localeID;
    source = new SearchSourceBuilder();
  }

  @Override
  public void paginate(int from, int size)
  {
    source.size(size);
    source.from(from);
  }

  private String prepareSortField(ISortDTO sort)
  {
    NestedPathBuilder pathBuilder = new NestedPathBuilder(Fields.propertyObjects.name());
    pathBuilder.append(Fields.attribute.name());
    pathBuilder.append(sort.getIsLanguageDependent() ? localeID : Fields.independent.name());
    String sortField = sort.getIsNumeric() ? Prefix.i_ + sort.getSortField() : Prefix.t_ + sort.getSortField();
    pathBuilder.append(sortField);
    pathBuilder.append(FieldType.raw.name());
    return pathBuilder.path();
  }

  @Override
  public void order(List<ISortDTO> sortOrder)
  {
    for (ISortDTO sort : sortOrder) {
      String prefix = prepareSortField(sort);
      String unmappedType = sort.getIsNumeric() ? "float" : "string";
      NestedSortBuilder nsb = new NestedSortBuilder(Fields.propertyObjects.name());
      FieldSortBuilder sortBuilder = SortBuilders.fieldSort(prefix)
          .order(SortOrder.fromString(sort.getSortOrder().name()))
          .unmappedType(unmappedType)
          .setNestedSort(nsb);
      source.sort(sortBuilder);
    }
  }

  @Override
  public void query(IQueryBuilder queryBuilder)
  {
    source.query(((QueryBuilder) queryBuilder).getQuery()) ;
  }

  @Override
  public void shouldFetchSource(Boolean shouldFetchSource)
  {
    source.fetchSource(shouldFetchSource);
  }

  @Override
  public void aggregate(IAggregator aggregation)
  {
    source.aggregation(((Aggregator)aggregation).getAggregationBuilder());
  }

  public SearchSourceBuilder getSource()
  {
    return source;
  }
}
