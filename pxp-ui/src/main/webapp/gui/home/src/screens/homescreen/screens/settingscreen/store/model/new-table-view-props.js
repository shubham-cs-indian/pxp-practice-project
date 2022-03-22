let NewTableViewProps = function () {

  let fProps = function () {
    return {
      headerData: [],
      rowData: [],
      isDirty: false,
    }
  };

  let oProperties = new fProps();

  return {
    setTableViewHeaderData: (aHeaderData) => {
      oProperties.headerData = aHeaderData;
    },

    getTableViewHeaderData: () => {
      return oProperties.headerData;
    },

    setTableViewRowData: (aRowData) => {
      oProperties.rowData = aRowData;
    },

    getTableViewRowData: () => {
      return oProperties.rowData;
    },

    getIsTableViewDataDirty: () => {
      return oProperties.isDirty;
    },

    setIsTableViewDataDirty: (bIsDirty) => {
      oProperties.isDirty = bIsDirty;
    },

    //Add functions above reset function
    reset: () => {
      oProperties = new fProps();
    }
  };
};

export default NewTableViewProps;