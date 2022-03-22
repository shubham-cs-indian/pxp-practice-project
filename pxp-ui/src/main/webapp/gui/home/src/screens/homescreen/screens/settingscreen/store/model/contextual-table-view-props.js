import NewTableViewProps from "./new-table-view-props";

let ContextualTableViewProps = ((() => {

  function Props() {
    return {};
  };

  let oProperties = new Props();

  return {
    resetTableViewPropsByContext(sContext) {
      delete oProperties[sContext];
    },

    getTableViewPropsByContext(sContext) {
      if (!sContext) {
        throw "Table view context not found";
      }
      return oProperties[sContext];
    },

    createTableViewPropsByContext(sContext) {
      if (!sContext) {
        throw "Table view context not found";
      }
      oProperties[sContext] = new NewTableViewProps();
      return  oProperties[sContext];
    },

    // Add functions above this.
    reset() {
      oProperties = new Props();
    }
  };
}))();

export default ContextualTableViewProps;