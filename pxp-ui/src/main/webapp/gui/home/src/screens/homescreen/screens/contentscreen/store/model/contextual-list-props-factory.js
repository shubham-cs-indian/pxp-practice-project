import CS from '../../../../../../libraries/cs';
import ContextualListProps from "./contextual-list-props";

class ContextualListPropsFactory {
  constructor () {
    this.props = {}
  }

  getPropsByContext (sContext) {
    if (CS.isNotEmpty(sContext)) {
      return this.props[sContext];
    } else {
      throw "Missing context"
    }
  }

  setPropsByContext (sContext, oRequestResponseInfo) {
    if (CS.isNotEmpty(sContext)) {
      this.props[sContext] = new ContextualListProps(sContext, oRequestResponseInfo);
      return this.props[sContext];
    } else {
      throw "Empty contextId"
    }
  }

  resetPropsByContext (sContext, oRequestResponseInfo = {}) {
    if (CS.isNotEmpty(sContext)) {
      this.props[sContext] = new ContextualListProps(sContext, oRequestResponseInfo);
      return this.props[sContext];
    }
  }

  removePropsByContext (sContext) {
    this.props = CS.omit(this.props, [sContext]);
  }

}

export default new ContextualListPropsFactory();