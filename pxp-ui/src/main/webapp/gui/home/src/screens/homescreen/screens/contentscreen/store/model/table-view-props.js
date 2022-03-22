import TableViewContextProps from './table-view-context-props';
import TableFilterViewContextProps from './table-filter-view-context-props';
import ContextVariantContextProps from './context-variant-context-props';
import ColumnOrganizerProps from '../../../../../../viewlibraries/columnorganizerview/contextual-column-organizer-view-props';
import CS from '../../../../../../libraries/cs';
import CommonUtils from './../../../../../../commonmodule/util/common-utils';

var TableViewProps = (function () {

  let sSpliter = CommonUtils.getSplitter();
  var Props = function () {};
  var oProperties = new Props();
  var oParentContextMap = {};
  let getUniquePropertyKey = function(sContextId, sParentId) {
    return (sContextId + sSpliter + sParentId);
  }

  return {
    createNewTableViewPropsByContext: function (sContextId, sParentId) {
      /** will create a new props within the main props, to use multiple table view implementations at the same time*/
      let sPropertyKey = getUniquePropertyKey(sContextId, sParentId);
      oProperties[sPropertyKey] = {
        tableViewProps: new TableViewContextProps(),
        tableFilterViewProps: new TableFilterViewContextProps(),
        variantContextProps: new ContextVariantContextProps(),
        columnOrganizerProps: new ColumnOrganizerProps(),
      };
      this.setContextByParentId(sContextId, sParentId);
    },

    resetTableFilterViewProps: function (sContextId, sParentId) {
      let sPropertyKey = getUniquePropertyKey(sContextId, sParentId);
      oProperties[sPropertyKey].tableFilterViewProps = new TableFilterViewContextProps();
    },

    getTableViewPropsByContext: function (sContextId, sParentId) {
      /** will return the props based on the context*/
      let sPropertyKey = getUniquePropertyKey(sContextId, sParentId);
      if (oProperties[sPropertyKey]) {
        return oProperties[sContextId + sSpliter + sParentId].tableViewProps;
      } else {
        return {};
      }
    },

    getTableFilterViewPropsByContext: function (sContextId, sParentId) {
      /** will return the props based on the context*/
      let sPropertyKey = getUniquePropertyKey(sContextId, sParentId);
      if (oProperties[sPropertyKey]) {
        return oProperties[sPropertyKey].tableFilterViewProps;
      } else {
        return {};
      }
    },

    getAllTableViewPropsByContext: function (sContextId, sParentId) {
      let sPropertyKey = getUniquePropertyKey(sContextId, sParentId);
      return oProperties[sPropertyKey];
    },

    getVariantContextPropsByContext: function (sContextId, sParentId) {
      /** will return the props based on the context*/
      let sPropertyKey = getUniquePropertyKey(sContextId, sParentId);
      if (oProperties[sPropertyKey]) {
        return oProperties[sPropertyKey].variantContextProps;
      } else {
        return {};
      }
    },

    getColumnOrganizerPropsByContext: function (sContextId, sParentId) {
      /** will return the props based on the context*/
      let sPropertyKey = getUniquePropertyKey(sContextId, sParentId);
      if (oProperties[sPropertyKey]) {
        return oProperties[sPropertyKey].columnOrganizerProps;
      } else {
        return {};
      }
    },

    resetPropsByContext: function (sContextId, sParentId) {
      let sPropertyKey = getUniquePropertyKey(sContextId, sParentId);
      delete oProperties[sPropertyKey];
    },

    setContextByParentId: function(sContextId, sParentId) {
      oParentContextMap[sParentId] = oParentContextMap[sParentId] || [];
      oParentContextMap[sParentId].push(sContextId);
    },

    getContextsByParentId: function(sParentId) {
      return oParentContextMap[sParentId];
    },

    resetContextsByParentId: function(sParentId) {
      CS.forEach(oParentContextMap[sParentId], sContextId => {
        let sPropertyKey = getUniquePropertyKey(sContextId, sParentId);
        delete oProperties[sPropertyKey];
      })
      delete oParentContextMap[sParentId];
    },

    reset: function () {
      oProperties = new Props();
    },

    resetLanguageDependentFilters: function(sParentId) {
    let aContextIds = TableViewProps.getContextsByParentId(sParentId);
      CS.forEach(aContextIds, sContextId => {
        let oTableFilterViewProps = TableViewProps.getTableFilterViewPropsByContext(sContextId, sParentId);
        if (CS.isNotEmpty(oTableFilterViewProps)) {
          let aAppliedFilterData = oTableFilterViewProps.getAppliedFilters();
          let oFilterInfo = oTableFilterViewProps.getFilterInfo();
          let aTranslatableAttributeIds = oFilterInfo.translatableAttributeIds;
          CS.remove(aAppliedFilterData, (oFilteredData) => {
            if (CS.includes(aTranslatableAttributeIds, oFilteredData.id)) {
              return true;
            }
          });
        }
      })
    },

    toJSON: function () {
      return {

      }
    }

  };

})();

export default TableViewProps;
