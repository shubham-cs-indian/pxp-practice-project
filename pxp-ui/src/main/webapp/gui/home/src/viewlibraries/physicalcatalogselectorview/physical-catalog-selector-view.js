import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import SessionProps from '../../commonmodule/props/session-props';
import { view as ContextMenuViewNew } from '../contextmenuwithsearchview/context-menu-view-new';
import ContextMenuViewModel from '../contextmenuwithsearchview/model/context-menu-view-model';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import PhysicalCatalogDictionary from '../../commonmodule/tack/physical-catalog-dictionary';

const oEvents = {
  HANDLE_PHYSICAL_CATALOG_OR_PORTAL_SELECTION_CHANGED: "handle_physical_catalog_or_portal_selection_changed"
};

const oPropTypes = {
  allowedPhysicalCatalogs: ReactPropTypes.array,
  showPhysicalCatalog: ReactPropTypes.bool,
  disabled: ReactPropTypes.bool,
  selectedPortal: ReactPropTypes.string
};
/**
 * @class PhysicalCatalogSelectorView - Used to select physical catalogs(PIM, onBoarding, offBoarding, PIM Archival). Selected physical catalog is showing on top bar.
 * @memberOf Views
 * @property {array} [allowedPhysicalCatalogs] - Contains all allowed physical catalogs data.
 * @property {bool} [showPhysicalCatalog] - To show physical catalog(Contains true to show physical catalog).
 * @property {bool} [disabled] - Used to disable physical catalog(Contains true to disable physical catalog).
 * @property {string} [selectedPortal] - Used to Display portal on the physical catalog selector view.
 */

// @CS.SafeComponent
class PhysicalCatalogSelectorView extends React.Component {

  constructor (props) {
    super(props);
  }

  getContextMenuModelList = (aItemList, sSelectedPhysicalCatalog) => {
    let aContextModelList = [];
    let _this = this;
    CS.forEach(aItemList, function (oItem) {
      let bIsActive = (oItem.id === sSelectedPhysicalCatalog);

      let oProperties = {
        customIconClassName: oItem.id
      };

      let sLabel = oItem.id !== PhysicalCatalogDictionary.PIM ? CS.getLabelOrCode(oItem) : getTranslation()[_this.props.selectedPortal.toUpperCase()];
      aContextModelList.push(new ContextMenuViewModel(
          oItem.id,
          sLabel,
          bIsActive,
          "",
          oProperties
      ));
    });

    return aContextModelList;
  };

  handlePhysicalCatalogSelectionChanged = (oModel) => {
    let sId = oModel.id;
    let aSelectedItems = sId ? [sId] : [];
    EventBus.dispatch(oEvents.HANDLE_PHYSICAL_CATALOG_OR_PORTAL_SELECTION_CHANGED, "physicalCatalog", aSelectedItems)
  };

  render () {
    if (!this.props.showPhysicalCatalog) {
      return null;
    }

    let bIsPhysicalCatalogSelectorDisabled = this.props.disabled;
    let sSelectedPhysicalCatalog = SessionProps.getSessionPhysicalCatalogId() || "";
    let sLabelForPhysicalCatalog = sSelectedPhysicalCatalog == "pimArchival" ? "archival": sSelectedPhysicalCatalog;
    let sLabelToDisplay = getTranslation()[this.props.selectedPortal.toUpperCase()];
    sLabelToDisplay = sSelectedPhysicalCatalog == "pim" ? sLabelToDisplay : sLabelToDisplay + ": " + getTranslation()[sLabelForPhysicalCatalog.toUpperCase()];
    let aPhysicalCatalogItems = this.props.allowedPhysicalCatalogs;
    CS.remove(aPhysicalCatalogItems, {id: "dataIntegration"});
    let aContextModelList = this.getContextMenuModelList(aPhysicalCatalogItems, sSelectedPhysicalCatalog);
    let sClassName = "physicalCatalogIcon " + sSelectedPhysicalCatalog;
    return (
        <div className="physicalCatalogSelectorView">

          <ContextMenuViewNew
              contextMenuViewModel={aContextModelList}
              showCustomIcon={true}
              disabled={bIsPhysicalCatalogSelectorDisabled}
              onClickHandler={this.handlePhysicalCatalogSelectionChanged}
              anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
              targetOrigin={{horizontal: 'left', vertical: 'top'}}
              showSearch={false}
          >
            <div className={bIsPhysicalCatalogSelectorDisabled ? "physicalCatalogSelectorIcon isDisabled" : "physicalCatalogSelectorIcon"}>
              <div className={sClassName}></div>
              <div className="physicalCatalogLabel">{sLabelToDisplay}</div>
              <div className="buttonIcon"></div>
            </div>
          </ContextMenuViewNew>

        </div>
    );
  }
}

PhysicalCatalogSelectorView.propTypes = oPropTypes;

export const view = PhysicalCatalogSelectorView;
export const events = oEvents;
