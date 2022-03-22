import FilterStore from "./new-filter-store";
import { omit } from './../../../../../../libraries/cs/cs-lodash';
import ContextualFilterProps from "../model/contextual-filter-props";
class FilterStoreFactory {
    constructor() {
        this.stores = {}
    }
    /**
     * @function getFilterStore
     * @description - Factory method for store
     * @param oFilterContext - filter context required for store.
     */
    getFilterStore(oFilterContext) {
        if (!oFilterContext || !oFilterContext.screenContext) {
            throw "Missing filter context"
        }
        if (!this.stores[oFilterContext.screenContext]) {
            this.stores[oFilterContext.screenContext] = new FilterStore(oFilterContext);
        }
        return this.stores[oFilterContext.screenContext];
    }
    /**
     * @function bindStoreAction
     * @description - To bind action to store.
     * @param action - action which will going to invoke.
     *  @param callback - callback function when action invoked.
     */
    bindStoreAction(action, callback) {
        /****** Prototype chaining not possible without creating an instance for function:
        Filter Store is function so we need to explicitly access bind through its prototype *****/
        FilterStore.prototype.bind(action, callback);
    }
    /**
     * @function resetStoreByContext
     * @description - Reset store and filter props by context.
     * @param sContext - Contains context to reset store and filter props.
     */
    resetStoreByContext(sContext) {
        ContextualFilterProps.resetFilterPropsByContext(sContext);
        this.stores = omit(this.stores, [sContext]);
    }
}

export default new FilterStoreFactory();