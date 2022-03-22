import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ContextMenuViewNew } from '../contextmenuwithsearchview/context-menu-view-new';
import { propTypes as ContextMenuViewNewProps } from '../contextmenuwithsearchview/context-menu-view-new';
import TooltipView from '../tooltipview/tooltip-view';
import { view as LazyContextMenuView } from '../lazycontextmenuview/lazy-context-menu-view';
import { view as CustomLazyContextMenuView } from '../lazycontextmenuview/custom-lazy-context-menu-view';
import {getTranslations as getTranslation} from "../../commonmodule/store/helper/translation-manager";
import {view as CustomDialogView} from "../customdialogview/custom-dialog-view";
import {view as TransferDialogView } from "../transferDialogView/transfer-dialog-view";

const aUseCases = ["directClick", "showList", "showDynamicList", "showCustomDynamicList"];

const oEvents = {
  HANDLE_BUTTON_CLICKED: "handle_button_clicked",
  LIST_ITEM_SELECTED: "list_item_selected",
  HANDLE_APPLY_CLICKED: "handle_apply_clicked",
  POPOVER_VISIBILITY_TOGGLED: "popover_visibility_toggled",
  HANDLE_DIRECT_CLICK_CLICKED: "handle_direct_click_clicked",
  TRANSFER_DIALOG_BUTTON_CLICKED: "transfer dialog button clicked"
};

const oPropTypes = {
  className: ReactPropTypes.string,
  context: ReactPropTypes.string,
  useCase: ReactPropTypes.oneOf(aUseCases).isRequired,
  tooltip: ReactPropTypes.string,
  contextMenuData: ReactPropTypes.shape(ContextMenuViewNewProps),
  isClickedNodeItemReturn: ReactPropTypes.bool,
  customObject: ReactPropTypes.object,
  tooltipPlacement: ReactPropTypes.string,
  filterContext: ReactPropTypes.object
};

/**
 * @class ButtonWithContextMenuView - Used to generate a drop down list.
 * @memberOf Views.
 * @property {string} [className] - To apply CSS className for button.
 * @property {string} [context]   - Describe which operation will be performed(for example: dataLanguage, transferContents, exportContents etc).
 * @property {custom}  usecase    - There are four use cases(directClick, showList, showCustomDynamicList, showDynamicList) used for rendering different views.
 * @property {string} [tooltip]   - Tooltip for ButtonWithContextMenuView.
 * @property {custom} [contextMenuData] - Contains isMultiSelect, requestResponseInfoData, contextMenuViewModel, showSearch, showArrowHead etc.
 * @property {bool}   [isClickedNodeItemReturn] - Indicate dispatched item or itemId when clicking on drop down item.
 * @property {object} [customObject] - Used to set label for drop down item.
 */

// @CS.SafeComponent
class ButtonWithContextMenuView extends React.Component {

  constructor (props) {
    super(props);

    this.handleButtonClicked = this.handleButtonClicked.bind(this);
    this.closePopover = this.closePopover.bind(this);
    this.onClickHandler = this.onClickHandler.bind(this);
    this.onApplyHandler = this.onApplyHandler.bind(this);
    this.getContextMenuView = this.getContextMenuView.bind(this);
    this.handleDirectClickClicked = this.handleDirectClickClicked.bind(this);
  }

  handleButtonClicked () {
    let sContext = this.props.context;
    EventBus.dispatch(oEvents.HANDLE_BUTTON_CLICKED, sContext);
  };

  closePopover (bPopoverVisibility, sContext) {
    EventBus.dispatch(oEvents.POPOVER_VISIBILITY_TOGGLED, bPopoverVisibility, sContext);
  };

  onClickHandler (oModel) {
    let sContext = this.props.context;
    if (this.props.isClickedNodeItemReturn) {
      EventBus.dispatch(oEvents.LIST_ITEM_SELECTED, oModel, sContext, this.props.filterContext);
    } else {
      EventBus.dispatch(oEvents.LIST_ITEM_SELECTED, oModel.id, sContext, this.props.filterContext);
    }
  };

  onApplyHandler (aSelectedIds) {
    EventBus.dispatch(oEvents.HANDLE_APPLY_CLICKED, aSelectedIds);
  };

  handleTransferDialogButtonClicked = (sContext , sButtonId) =>{
    EventBus.dispatch(oEvents.TRANSFER_DIALOG_BUTTON_CLICKED ,sContext ,sButtonId);
  };

  handleDirectClickClicked () {
    let sContext = this.props.context;
    let oFilterContext = this.props.filterContext;
    let sId = this.props.contextMenuData.contextMenuViewModel[0].id;
    EventBus.dispatch(oEvents.HANDLE_DIRECT_CLICK_CLICKED, sId , sContext , oFilterContext);
  };

  getContextMenuView () {
    let __props = this.props;
    let sClassName = "buttonWithContextMenuViewWrapper";
    let oContextMenuData = __props.contextMenuData;
    let fShowHidePopoverHandler = CS.isBoolean(oContextMenuData.showPopover) ? this.closePopover : null;

    switch (__props.useCase) {

      case "directClick":
        return (
            <TooltipView label={__props.tooltip}  placement={__props.tooltipPlacement || "top"}>
              <div className={sClassName} onClick={this.handleDirectClickClicked}>
              </div>
            </TooltipView>
        );

      case "showList":
        return (
            <ContextMenuViewNew
                {...oContextMenuData}
                showHidePopoverHandler={fShowHidePopoverHandler}
                onClickHandler={this.onClickHandler}
                onApplyHandler={this.onApplyHandler}>
              <TooltipView label={__props.tooltip} placement={__props.tooltipPlacement || "top"}>
                <div className={sClassName} onClick={oContextMenuData.disabled ? null : this.handleButtonClicked}>
                </div>
              </TooltipView>
            </ContextMenuViewNew>
        );

      case "showDynamicList":
        let sContext = __props.context;
        if (sContext === "transferContents") {
          let oBodyStyle = {
            maxWidth: "1200px",
            minWidth: "880px",
          };

          let aButtonData = [
            {
              id: "cancel",
              label: getTranslation().CANCEL,
              isDisabled: false,
              isFlat: false,
            },
            {
              id: "transfer",
              label: getTranslation().TRANSFER,
              isFlat: false,
              isDisabled: false
            }];

          let sTitle = getTranslation().TRANSFER;
          let bIsDialogOpen = this.props.contextMenuData.showPopover;
          let oExportView =  <TransferDialogView contextMenuData={this.props.contextMenuData}/>;
          return (
              <div className='transferButtonView'>
                <CustomDialogView modal={true}
                                  open={bIsDialogOpen}
                                  title={sTitle}
                                  bodyStyle={oBodyStyle}
                                  contentStyle={oBodyStyle}
                                  buttonData={aButtonData}
                                  onRequestClose={this.handleTransferDialogButtonClicked.bind(this)}
                                  buttonClickHandler={this.handleTransferDialogButtonClicked.bind(this, sContext )}>
                  {oExportView}
                </CustomDialogView>
                <TooltipView label={__props.tooltip} placement={__props.tooltipPlacement || "top"}>
                  <div className={sClassName} onClick={this.handleButtonClicked}>
                  </div>
                </TooltipView>
              </div>
          );
        } else {
          return (
              <LazyContextMenuView
                  anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
                  targetOrigin={{horizontal: 'left', vertical: 'top'}}
                  showPopover={oContextMenuData.showPopover}
                  requestResponseInfo={oContextMenuData.requestResponseInfoData}
                  showHidePopoverHandler={fShowHidePopoverHandler}
                  isMultiselect={oContextMenuData.isMultiSelect}
                  onClickHandler={this.onClickHandler}
                  onApplyHandler={this.onApplyHandler}
                  showSearch={oContextMenuData.showSearch}
              >
                <TooltipView label={__props.tooltip} placement={__props.tooltipPlacement || "top"}>
                  <div className={sClassName} onClick={this.handleButtonClicked}>
                  </div>
                </TooltipView>
              </LazyContextMenuView>
          );
        }

      case "showCustomDynamicList":
        let oAnchorOrigin = {horizontal: 'left', vertical: 'bottom'};
        if (this.props.anchorOrigin) {
          oAnchorOrigin = this.props.anchorOrigin;
        }

        let oTargetOrigin = {horizontal: 'left', vertical: 'top'};
        if (this.props.targetOrigin) {
          oTargetOrigin = this.props.targetOrigin;
        }
        let fButtonClickHandler = this.props.isDisabled ? null : this.handleButtonClicked;
        sClassName += this.props.isDisabled ? " disabled" : "";

        return (
            <CustomLazyContextMenuView
                anchorOrigin={oAnchorOrigin}
                targetOrigin={oTargetOrigin}
                showPopover={oContextMenuData.showPopover}
                requestResponseInfo={oContextMenuData.requestResponseInfoData}
                showHidePopoverHandler={fShowHidePopoverHandler}
                isMultiselect={oContextMenuData.isMultiSelect}
                onClickHandler={this.onClickHandler}
                onApplyHandler={this.onApplyHandler}
                customObject = {this.props.customObject}
                context={this.props.context}
                disabled={oContextMenuData.disabled || this.props.isDisabled}
                excludedItems={oContextMenuData.excludedItems}
                showDefaultIcon={oContextMenuData.showDefaultIcon}
            >
              <TooltipView label={__props.tooltip} placement={__props.tooltipPlacement || "top"}>
                <div className={sClassName} onClick={fButtonClickHandler}>
                </div>
              </TooltipView>
            </CustomLazyContextMenuView>
        );
    }
  };

  render () {
    let __props = this.props;
    let sClassName = "buttonWithContextMenuViewContainer " + __props.className;
    return (
        <div className={sClassName}>
          {this.getContextMenuView()}
          {this.props.children}
        </div>
    )
  }
}

ButtonWithContextMenuView.propTypes = oPropTypes;

export const view = ButtonWithContextMenuView;
export const events = oEvents;
