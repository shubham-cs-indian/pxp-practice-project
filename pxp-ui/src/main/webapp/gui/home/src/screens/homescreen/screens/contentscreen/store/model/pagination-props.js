const PaginationProps = function () {

  let Props = function () {
    return {
      iFrom: 0,
      iPageSize: 20,
      iCurrentPageItems: 0,
      iTotalCount: 0,
    }
  };

  let oProperties = new Props();

  this.getFromValue = function () {
    return oProperties.iFrom;
  };

  this.setFromValue = function (_iFrom) {
    oProperties.iFrom = _iFrom;
  };

  this.getPaginationSize = function () {
    return oProperties.iPageSize;
  };

  this.setPaginationSize = function (_iPageSize) {
    oProperties.iPageSize = _iPageSize;
  };

  this.getCurrentPageItems = function () {
    return oProperties.iCurrentPageItems
  };

  this.setCurrentPageItems = function (_iCurrentPageItems) {
    oProperties.iCurrentPageItems = _iCurrentPageItems;
  };

  this.getTotalItemCount = function () {
    return oProperties.iTotalCount;
  };

  this.setTotalItemCount = function (_iTotalCount) {
    oProperties.iTotalCount = _iTotalCount;
  };

  this.resetPaginationData = function () {
    oProperties = new Props();
  };
};

export default PaginationProps;