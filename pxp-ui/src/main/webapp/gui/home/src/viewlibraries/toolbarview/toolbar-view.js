import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as ButtonView } from '../buttonview/button-view';
import { view as CreateButtonView } from '../createbuttonview/create-button-view';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import oFilterPropType from '../../screens/homescreen/screens/contentscreen/tack/filter-prop-type-constants';
import {view as CustomMaterialButtonView} from "../custommaterialbuttonview/custom-material-button-view";

const oEvents = {
  // TOOLBAR_VIEW_BUTTON_CLICKED: "toolbar_button_clicked"
};

const oPropTypes = {
  sortViewData: ReactPropTypes.object,
  toolbarData: ReactPropTypes.object,
  viewMasterData: ReactPropTypes.object,
  selectedItem: ReactPropTypes.object,
  createButtonData: ReactPropTypes.object,
  customCreateButtonData: ReactPropTypes.object,
  viewContext: ReactPropTypes.object,
  filterContext: ReactPropTypes.object,
  buttonClickHandler: ReactPropTypes.func
};
/**
 * @class ToolbarView - use to display toolbar item in Application.
 * @memberOf Views
 * @property {object} [sortViewData] -  object contain sort view data.
 * @property {object} [toolbarData] -  object contain toolBar Data.
 * @property {object} [viewMasterData] -  object contain allowed view array.
 * @property {object} [selectedItem] -  object contain selectItem.
 * @property {object} [createButtonData] -  object contain context and list of array of button data.
 * @property {object} [viewContext] -  object contain sort view context.
 */

// @CS.SafeComponent
class ToolbarView extends React.PureComponent {

  constructor (props) {
    super(props);
  }

  /* NOTE: createButtonGroup function accepts both array and object as Parameters */
  createButtonGroup = (aButtonGroups) => {
    let oButtonGroupsDOM = [];
    let _this = this;
    let _props = this.props;
    // TODO: remove dummy button addition
/*
    let temp = ContentScreenStore.getToolbarButtons();
    aButtonGroups.temp = temp;
*/

    CS.forEach(aButtonGroups, function (oGroup, iGroupIndex) {
      let aClubButtons = [];
      if(oGroup.length <= 0){
        return;
      }
      oGroup = _this.getToolItemsFromList(oGroup);

      CS.forEach(oGroup, function (oButton, iButtonIndex) {
        let oButtonDOM;

        /*TODO : remove this section/ discussion needed*/
        if(oButton.isVisible === false) {
          return;
        }
        let oViewContext = oButton.viewContext || _this.props.viewContext;
        oButtonDOM = (
              <ButtonView
                  /* --------- required for rendering the buttons ------*/
                  id={oButton.id}                              /* Required field*/
                  key={oButton.id + "_" + oButton.label + "_" + iButtonIndex}        /* concatenating id with label and index for uniqueness*/
                  label={CS.getLabelOrCode(oButton)}                        /* Text to display on button, MUST REQUIRED IN CASE ON CONTEXT MENU BUTTON
                                                                 Don't the value in case of icon buttons, as this would overwrite the icon class provided */
                  showLabel={oButton.showLabel}                // if button contain icon, then to show label or not (default false-don't show label)
                  tooltip={oButton.tooltip}                    // if tooltip is required, then can pass a string to it
                  theme={oButton.theme}                        // possible values - light(default), blue, dark */}
                  /* Button flags
                  * active - highlighted,             - toggle -> becomes inactive
                  * inactive - non-highlighted (faded)- toggle -> becomes active
                  * disabled - displayed, but no pointer events and faded -> no clicks allowed
                  * visibility - true ----------- false
                  *                |                |
                  *                v                v
                  *              show button      hide button
                  * */
                  isActive={oButton.isActive}
                  /* if false, then button is inactive (remains useless if disabled)
                  *  Menu button can't be inactive, as it has click event associated to it.
                  *  Menu buttons can be disabled to limit their click events.
                  * */

                  showSeparate={oButton.showSeparate}         // To show the buttons separate.
                  isDisabled={oButton.isDisabled}             // if false, then button is disabled (buttons active state becomes useless)

                  /*TODO : comment isVisible section*/
                  isVisible={oButton.isVisible}              // handled from toolbar-view itself in case if the value is false, so not required to pass to button-view
                                                             // if visible == true, then only this field is required, default value would be true
                  viewContext={oViewContext}
                  isViewSelected={oButton.isViewSelected}    // show the current selected view

                  /*  --------- required to render icon buttons -------- */
                  className={oButton.className}                // pass icon class (css) */}
                  type={oButton.type}                          // pass the type - simple(default) or menu */}

                  /*  --------- required for context menu buttons ------- */
                  contextMenuViewModel={oButton.contextMenuViewModel} // MUST REQUIRED FOR RENDERING CONTEXT MENU BUTTON */}

                  selectedItemsCount={oButton.selectedCount}   // calculated at toolbar and is passed in props */}

                  /*  click handler is required if the buttons are not having any default click events
                       either of two click/apply - one is required for context menu */
                  clickHandler={oButton.clickHandler}
                  applyHandler={oButton.applyHandler}

                  isMultiSelect={oButton.isMultiSelect}
                  loadMoreHandler={oButton.loadMoreHandler}

                  /*  --------- following are contextMenu defaults. ------- */
                  searchHandler={oButton.searchHandler}
                  searchText={oButton.searchText}
                  /*  style={button.oStyle}                    // this can be used if you want some specific css styles to be applied */
                  showSelectedItems={oButton.showSelectedItems}
                  showColor={oButton.showColor}
                  showCustomIcon={oButton.showCustomIcon}
                  isMovable={oButton.isMovable}
                  menuListHeight={oButton.menuListHeight}
                  selectedItems={oButton.selectedItems}
                  onItemsChecked={oButton.onItemsChecked}
                  showSearch={oButton.showSearch}
                  view={oButton.view}
                  onChange={oButton.onChange || _props.buttonClickHandler || null}
                  filterContext={_props.filterContext}
              />
          );

        aClubButtons.push(oButtonDOM);
      });

      let oClubDOM = (
        <div key={`clubDOM${iGroupIndex}`} className={ iGroupIndex +"  clubbedButtonsWrapper "} id={"clubbedButtons-" + iGroupIndex}>
            {aClubButtons}
          </div>
      );

      oButtonGroupsDOM.push(oClubDOM);
    });

    return oButtonGroupsDOM
  };

  getToolItemsFromList = (aList) => {
    var _this = this;
    var aItems = [];
    var sSelectedViewMode = _this.props.viewContext ? _this.props.viewContext.selectedViewMode : "";
    CS.forEach(aList, function (oItem, iIndex) {
      if(oItem.isViewSelected){
        oItem.isViewSelected = "";
      }

      if (oItem.view) {
        aItems.push(oItem);
      }
      else {
        //TODO: Cleanup code for making selected
        let sClassName = "";
        if ((sSelectedViewMode === oItem.className) || (oItem.isSelected)) {
          sClassName = " selected";
          oItem.isViewSelected = sClassName;
        }
        aItems.push(oItem);
      }
    });
    return aItems;
  };

  getCreateButtonView = () => {
    let _props = this.props;
    let oCreateButtonData = _props.createButtonData;
    if (oCreateButtonData && !oCreateButtonData.isHideCreateButton) {
      return (
          <CreateButtonView
              context={oCreateButtonData.context}
              dragDropContext={oCreateButtonData.dragDropContext}
              itemList={oCreateButtonData.list}
              placeholder={oCreateButtonData.placeholder}
              filterContext={_props.filterContext}
              searchText={oCreateButtonData.searchText}
          />);
    } else {
      return null;
    }
  };

  getAddEntityButtonView = () => {
    let _this = this;
    let _props = _this.props;
    let oCreateButtonData = _props.createButtonData;
    if (oCreateButtonData && oCreateButtonData.showAddButtonForCollection) {
      let oFilterContext = {
        filterType: oFilterPropType.QUICKLIST,
        screenContext: oFilterPropType.QUICKLIST,
      }
      return (
        <div className={"clubbedButtonsWrapper"}>
          <ButtonView id={"staticCollectionAddEntity"}
                      showLabel={false}
                      tooltip={getTranslation().ADD_ENTITY}
                      placement={"top"}
                      isDisabled={false}
                      className={"addEntityButton"}
                      type={"simple"}
                      filterContext={oFilterContext}/>
        </div>
      );
    } else {
      return null;
    }
  };

  getCustomCreateButton = () => {
    let oCustomCreateButtonData = this.props.customCreateButtonData;
    if(CS.isNotEmpty(oCustomCreateButtonData)) {
      return (
          <div className={oCustomCreateButtonData.className}>
            <CustomMaterialButtonView
                label={oCustomCreateButtonData.label}
                isRaisedButton={oCustomCreateButtonData.isRaisedButton}
                isDisabled={oCustomCreateButtonData.isDisabled}
                onButtonClick={oCustomCreateButtonData.onButtonClick}
            />
          </div>
      );
    }
    return null;
  }

  render () {
    // let oButtonGroupsDOM = this.createButtonGroup([]); // this.props.toolbarData);
    let oCreateButtonView = this.getCreateButtonView();
    let oButtonGroupsDOM = this.createButtonGroup(this.props.toolbarData);
    let oAddEntityButtonView = this.getAddEntityButtonView();
    let oCustomCreateButton = this.getCustomCreateButton();

    return (
      <div className="toolbarViewContainer">
        {oAddEntityButtonView}
        {oCreateButtonView}
        {oCustomCreateButton}
        {oButtonGroupsDOM}
      </div>
    );
  }
}

ToolbarView.propTypes = oPropTypes;

export const view = ToolbarView;
export const events = oEvents;
