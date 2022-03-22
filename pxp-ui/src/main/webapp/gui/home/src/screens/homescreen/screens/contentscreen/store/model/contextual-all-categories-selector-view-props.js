import AllCategoriesSelectorViewProps from "./all-categories-view-props"

let ContextualAllCategoriesSelectorViewProps = ((() => {

  function Props() {
    return {};
  };

  let oProperties = new Props();

  return {
    getAllCategoriesSelectorViewPropsByContext(sContext) {
      if (!sContext) {
        throw "All Categories view context not found";
      }
      return oProperties[sContext] || this.createAllCategoriesSelectorViewPropsByContext(sContext)
    },

    createAllCategoriesSelectorViewPropsByContext(sContext) {
      if (!sContext) {
        throw "All Categories view context not found";
      }
      oProperties[sContext] = new AllCategoriesSelectorViewProps();
      return  oProperties[sContext];
    },

    // Add functions above this.
    reset() {
      oProperties = new Props();
    }
  };
}))();

export default ContextualAllCategoriesSelectorViewProps;