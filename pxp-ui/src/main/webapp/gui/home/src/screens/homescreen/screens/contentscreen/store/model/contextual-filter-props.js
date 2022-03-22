import CollectionFilterProps from "./context-collection-filter-props";
import HierarchicalFilterProps from "./hierarchical-filter-props";
import ModuleFilterProps from "./module-filter-props";
import QuickListFilterProps from "./quicklist-filter-props";
import PaginationProps from "./pagination-props";
import { omit } from './../../../../../../libraries/cs/cs-lodash';
import oFilterPropType from './../../tack/filter-prop-type-constants';

const FilterTypes = {
  [oFilterPropType.COLLECTION] : CollectionFilterProps,
  [oFilterPropType.HIERARCHY] : HierarchicalFilterProps,
  [oFilterPropType.MODULE] : ModuleFilterProps,
  [oFilterPropType.QUICKLIST] : QuickListFilterProps,
  [oFilterPropType.PAGINATION] : PaginationProps
};


let ContextualFilterProps = ((() => {

  function Props() {
    return {};
  };

  let oProperties = new Props();

  return {

    /**
     * @function resetFilterPropsByContext
     * @description - Reset filter props by context.
     * @param sContext - Contains context to reset filter props.
     */
    resetFilterPropsByContext(sContext) {
      oProperties = omit(oProperties, [sContext]);
    },

    /**
     * @function getFilterPropsByContext
     * @description - Used to get filter props by context.
     * @param sContext - Contains context to get filter props.
     * @param sType - Contains type of filter, It should be COLLECTION, HIERARCHY, MODULE, QUICKLIST, PAGINATION etc.
     * @returns {*}
     */
    getFilterPropsByContext(sContext, sType) {
      if (!sContext) {
        throw "Context not found";
      }
      if (!oProperties[sContext]) {
        oProperties[sContext] = new FilterTypes[sType]();
      }
      return oProperties[sContext];
    },

    /**
     * @function reset
     * @description - Reset all filters props
     */
    reset() {
      oProperties = new Props();
    }
  };
}))();

export default ContextualFilterProps;