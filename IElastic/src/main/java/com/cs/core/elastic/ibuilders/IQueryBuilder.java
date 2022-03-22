package com.cs.core.elastic.ibuilders;

import com.cs.core.rdbms.config.idto.IPropertyDTO;

/**
 * The responsibility of this class is to provide atomic filters that can be used
 * with flexibility and interchangeably. Any new type of Filter should be introduced here.
 *
 * @Author Niraj.Dighe.
 *
 */
public interface IQueryBuilder {

  /**
   *  Add basic filters like organizationCode, CatalogCode, endpointCode.
   * @return builder to query.
   */
  public IQueryBuilder baseFilter();

  /**
   * Add filter for permissions.
   * @return builder of query.
   */
  public IQueryBuilder permissions();

  /**
   * Add filter for classifier.
   * @return builder for query.
   */
  public IQueryBuilder classifiers();

  /**
   * Add filter for simple search.
   * @return builder for query.
   */
  public IQueryBuilder simpleSearch();

  /**
   * Add filter for advance search.
   * @return  builder for query.
   */
  public IQueryBuilder advanceSearch();
  
  /**Add Filters for Rule Violations.
   * @return builder for query.
   */
  public IQueryBuilder ruleViolationFilters();

  /** Add filter for excluding selected ids
     * @return builder for query
     */
  public IQueryBuilder excludeIds();
  
  /**
   * Add filter for Collection.
   * @return  builder for query.
   */
  
  public IQueryBuilder collectionFilter(); 
  
  /**
   * Add expired filter.
   * @return
   */
  public IQueryBuilder expiredFilter();
  
  /**
   * Add duplicate filter
   * @return
   */
  public IQueryBuilder duplicateFilter();

  /**
   *
   * @return property Existence
   */
  IQueryBuilder propertyExistence(IPropertyDTO propertyDTO);
  
  public IQueryBuilder isSearchable();

  IQueryBuilder contextFilter();
  IQueryBuilder parentFilter();

  IQueryBuilder timeRange();
}
