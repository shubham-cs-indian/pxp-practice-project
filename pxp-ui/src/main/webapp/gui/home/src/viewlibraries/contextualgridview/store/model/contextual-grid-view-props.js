import ColumnOrganizerProps from "../../../columnorganizerview/contextual-column-organizer-view-props";
import GridViewProps from "./grid-view-props"
import CS from '../../../../libraries/cs';

let ContextualGridViewProps = ((() => {

  function Props() {
    return {};
  };

  let oProperties = new Props();

  return {
    resetGridViewPropsByContext(sContext) {
      delete oProperties[sContext];
    },

    getGridViewPropsByContext(sContext) {
      if (!sContext) {
        throw "Grid view context not found";
      }
      return oProperties[sContext];
    },

    createGridViewPropsByContext(sContext) {
      if (!sContext) {
        throw "Grid view context not found";
      }
      oProperties[sContext] = {
        gridViewProps: new GridViewProps(),
        columnOrganizerProps: new ColumnOrganizerProps(),
      };
      return  oProperties[sContext];
    },

    checkIsAnyGridDataDirty () {
      let bIsGridDataDirty = false;
      CS.forEach(oProperties, (oProperty) => {
        let oGridViewProps = oProperty.gridViewProps;
        if (oGridViewProps.getIsGridDataDirty()) {
          bIsGridDataDirty = true;
          return false;
        }
      });
      return bIsGridDataDirty;
    },

    // Add functions above this.
    reset() {
      oProperties = new Props();
    }
  };
}))();

export default ContextualGridViewProps;