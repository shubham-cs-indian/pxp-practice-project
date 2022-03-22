package com.cs.core.elastic.builders;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;

import com.cs.core.elastic.ibuilders.IQueryBuilder;
import com.cs.core.rdbms.entity.idto.ISearchDTO;

public class ClassifierQueryBuilder extends QueryBuilder {

  protected ClassifierQueryBuilder(ISearchDTO searchDto, BoolQueryBuilder query)
  {
    super(searchDto, query);
  }

  public IQueryBuilder permissions()
  {
    if (!searchDTO.shouldCheckPermissions()) {
      return this;
    }
    BoolQueryBuilder permissionQuery = new BoolQueryBuilder();
    if (!searchDTO.getClassesWithReadPermission().isEmpty()) {
      TermsQueryBuilder typesQuery = QueryBuilders.termsQuery(SearchBuilder.Fields.classIds.name(), searchDTO.getClassesWithReadPermission());
      permissionQuery.must(typesQuery);
    }
    if (!searchDTO.getTaxonomiesWithReadPermission().isEmpty()) {
      TermsQueryBuilder taxonomyIdsExistQuery = QueryBuilders.termsQuery(SearchBuilder.Fields.taxonomyIds.name(),
          searchDTO.getTaxonomiesWithReadPermission());
      
      BoolQueryBuilder taxonomyIdsEmptyQuery = new BoolQueryBuilder();
      
      BoolQueryBuilder majorTaxonomyMustNotAvailableQuery = new BoolQueryBuilder();
      majorTaxonomyMustNotAvailableQuery.mustNot(QueryBuilders.existsQuery(SearchBuilder.Fields.taxonomyIds.name()));
      taxonomyIdsEmptyQuery.should(majorTaxonomyMustNotAvailableQuery);
      
      BoolQueryBuilder majorTaxonomyIdsQuery = QueryBuilders.boolQuery();
      BoolQueryBuilder majorTaxonomyIdsBatchQuery = QueryBuilders.boolQuery();
      
      List<List<String>> listsOfMajorTaxonomyIds = ListUtils.partition(searchDTO.getMajorTaxonomyIds(), 1000);
      for (List<String> majorTaxonomyList : listsOfMajorTaxonomyIds) {
        majorTaxonomyIdsBatchQuery.should(QueryBuilders.termsQuery(SearchBuilder.Fields.taxonomyIds.name(), majorTaxonomyList));
      }
      majorTaxonomyIdsBatchQuery.minimumShouldMatch(1);
      majorTaxonomyIdsQuery.mustNot(majorTaxonomyIdsBatchQuery);
      
      taxonomyIdsEmptyQuery.minimumShouldMatch(1);
      taxonomyIdsEmptyQuery.should(majorTaxonomyIdsQuery);
      
      BoolQueryBuilder taxonomyIdsQuery = new BoolQueryBuilder();
      taxonomyIdsQuery.should(taxonomyIdsExistQuery);
      if (StringUtils.isEmpty(searchDTO.getKpiId())) {
        taxonomyIdsQuery.should(taxonomyIdsEmptyQuery);
      }

      taxonomyIdsQuery.minimumShouldMatch(1);
      permissionQuery.must(taxonomyIdsQuery);
    }
    query.filter(permissionQuery);

    return this;
  }

  public IQueryBuilder classifiers()
  {
    boolean isFromChooseTaxonomy = searchDTO.getIsFromChooseTaxonomy();
    if(isFromChooseTaxonomy) {
      List<String> classifierCodes = searchDTO.getClassIds();
      if(!classifierCodes.isEmpty()){
        query.should(QueryBuilders.termsQuery(SearchBuilder.Fields.classIds.name(), classifierCodes));
      }
      classifierCodes = searchDTO.getTaxonomyIds();
      if(!classifierCodes.isEmpty()){
        query.should(QueryBuilders.termsQuery(SearchBuilder.Fields.taxonomyIds.name(), classifierCodes));
      }
      query.minimumShouldMatch(1);
    }
    else {
      List<String> classifierCodes = searchDTO.getClassIds();
      if(!classifierCodes.isEmpty()){
        query.filter(QueryBuilders.termsQuery(SearchBuilder.Fields.classIds.name(), classifierCodes));
      }
      classifierCodes = searchDTO.getTaxonomyIds();
      if(!classifierCodes.isEmpty()){
        query.filter(QueryBuilders.termsQuery(SearchBuilder.Fields.taxonomyIds.name(), classifierCodes));
      }
    }
    return this;
  }


}
