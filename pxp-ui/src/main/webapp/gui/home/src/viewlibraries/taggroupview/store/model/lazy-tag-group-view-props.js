class LazyTagGroupViewProps {

  static defaultPaginationData = {
    from: 0,
    size: 20,
    sortBy: 'label',
    sortOrder: 'asc',
    searchColumn: 'label',
    searchText: ''
  };

  constructor (oReferencedData, oCustomRequestObject) {
    this.referencedData = oReferencedData;
    this.paginationData = LazyTagGroupViewProps.defaultPaginationData;
    this.itemsData = [];
    this.itemsDataMap = {};
    this.isSearchApplied = false;
    this.customRequestObject = oCustomRequestObject;
  }

  getReferencedData () {
    return this.referencedData;
  };

  getItemsData () {
    return this.itemsData;
  };

  setItemsData (aItemsData) {
    this.itemsData = aItemsData;
  };

  getItemsDataMap () {
    return this.itemsDataMap;
  };

  setItemsDataMap (aItemsDataMap) {
    this.itemsDataMap = aItemsDataMap;
  };

  getIsSearchApplied () {
    return this.isSearchApplied;
  };

  setIsSearchApplied (bIsSearchApplied) {
    this.isSearchApplied = bIsSearchApplied;
  };

  getPaginationData () {
    return this.paginationData;
  };

  resetPaginationData () {
    this.paginationData = LazyTagGroupViewProps.defaultPaginationData;
  };

  getCustomRequestObject () {
    return this.customRequestObject;
  };
}

export default LazyTagGroupViewProps;