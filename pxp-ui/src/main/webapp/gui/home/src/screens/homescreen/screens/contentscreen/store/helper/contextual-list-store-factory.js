import CS from '../../../../../../libraries/cs';
import ContextualListStore from "./contextual-list-store";

class ContextualListStoreFactory {
  constructor () {
    this.stores = {}
  }

/*  static triggerChange () {
    ContextualListStoreFactory.trigger('contextual-list-factory-change');
  };*/

  getStore (sContext) {
    if (CS.isNotEmpty(sContext)) {
      return this.stores[sContext];
    } else {
      throw "Missing  context"
    }
  }

  bindStoreAction (action, callback) {
    ContextualListStore.prototype.bind(action, callback);
  }

  setStore (sContext, oRequestResponseInfo, fTriggerChangeHandler) {
    if (CS.isNotEmpty(sContext)) {
      this.stores[sContext] = new ContextualListStore(sContext, oRequestResponseInfo, fTriggerChangeHandler);
      return this.stores[sContext];
    } else {
      throw "Empty contextId"
    }
  }

  resetStoreByContext (sContextId) {
    this.stores = CS.omit(this.stores, [sContextId]);
  }

}

export default new ContextualListStoreFactory();