import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import TooltipView from './../tooltipview/tooltip-view';
import { view as ImageFitToContainerView } from './../imagefittocontainerview/image-fit-to-container-view';
import copy from "copy-to-clipboard";
import alertify from "../../commonmodule/store/custom-alertify-store";
import {getTranslations} from "../../commonmodule/store/helper/translation-manager";

const oEvents = {
  LIST_NODE_CLICKED: "list_node_clicked",
  LIST_NODE_VIEW_DELETE_NODE_CLICKED: "list_node_view_delete_node_clicked",
  LIST_NODE_VIEW_CLONE_NODE_CLICKED: "list_node_view_clone_node_clicked"
};

const oPropTypes = {
  model: ReactPropTypes.object.isRequired
};
/**
 * @class ListNodeView - ListNodeView used to present list of items in standard format.
 * @memberOf Views
 * @property {object} model - Contains view which will render inside ListNodeView.
 */

// @CS.SafeComponent
class ListNodeView extends React.Component {

  constructor(props) {
    super(props);
  }

  handleClickOnListNode = (oModel, oEvent) => {
    if (!oEvent.nativeEvent.dontRaise) {
        EventBus.dispatch(oEvents.LIST_NODE_CLICKED, this, oEvent, oModel);
      }
  };

  getNodeNameView =(oListModel)=> {
    let bShowId = oListModel.properties["showId"];
    let sLabel = CS.getLabelOrCode(oListModel);
    let aCodeLabels = [];
    if(bShowId) {
      let sId = oListModel.properties["code"] ? oListModel.properties["code"] : oListModel.id;
      aCodeLabels.push(<div className="nodeId" key={sId}>{sId}</div>);
    }
    let oListNodeNameView = (
        <div className="listNodeViewName">
          <div className="list-label">{sLabel}</div>
          {aCodeLabels}
        </div>
    );

    return oListNodeNameView;
  }

  handleCopyToClipboardClick = (sValue) => {
    copy(sValue) ? alertify.success(getTranslations().CODE_COPIED) : alertify.error(getTranslations().COPY_TO_CLIPBOARD_FAILED);
  }

  handleDeleteNodeButtonClicked (sId, oEvent) {
    oEvent.stopPropagation();
    EventBus.dispatch(oEvents.LIST_NODE_VIEW_DELETE_NODE_CLICKED, sId, this.props.context);
  };

  handleCreateCloneNodeButtonClicked (sId, oEvent) {
    oEvent.stopPropagation();
    EventBus.dispatch(oEvents.LIST_NODE_VIEW_CLONE_NODE_CLICKED, sId);
  };

  render() {

    let oListModel = this.props.model;
    let sIconClassName = "listViewIcon ";
    let sNodeClassName = oListModel.isSelected || oListModel.isChecked ? "listNodeSelected " : " ";
    let oListNodeNameView = this.getNodeNameView(oListModel);

    let oIcon = oListModel.properties["hideLeftIcon"] ? null : (
          <div className={sIconClassName}>
            <ImageFitToContainerView imageSrc={oListModel.icon}/>
          </div>
    );

    let sId = oListModel.properties["code"] ? oListModel.properties["code"] : oListModel.id;
    let oCopyToClipboardDOM = (
        <TooltipView placement={"top"} label={getTranslations().COPY_TO_CLIPBOARD_TOOLTIP}>
          <div className={"copyToClipboard"} onClick={this.handleCopyToClipboardClick.bind(this, sId)}></div>
        </TooltipView>
    );
    let oDeleteDOM = (
        oListModel.properties["deleteIconVisibility"] ?
        <TooltipView placement={"top"} label={getTranslations().DELETE}>
          <div className={"deleteOrganization"} onClick={this.handleDeleteNodeButtonClicked.bind(this, sId)}></div>
        </TooltipView> : null
    );

    let oCloneDOM = (
        oListModel.properties["createCloneIconVisibility"] ?
        <TooltipView placement={"top"} label={getTranslations().CREATE_CLONE}>
          <div className={"createClone"} onClick={this.handleCreateCloneNodeButtonClicked.bind(this, sId)}/>
        </TooltipView> : null
    )

    let bShowId = oListModel.properties["showId"];
    let sLabel = CS.getLabelOrCode(oListModel);
    let aLabels = [sLabel];
    if(bShowId) {
      let sId = oListModel.properties["code"] ? oListModel.properties["code"] : oListModel.id;
      aLabels.push(<span className="tooltipNodeId" key={sId}>{sId}</span>);
    }

    return (
        <TooltipView label={aLabels}>
          <div className={"listNodeViewContainer " + sNodeClassName}
               onClick={this.handleClickOnListNode.bind(this, oListModel)}>
            {oIcon}
            <div className="listViewWrapper">
              {oListNodeNameView}
              <div className="listViewActionItemButtons">
                {oCloneDOM}
                {oDeleteDOM}
                {oCopyToClipboardDOM}
              </div>
            </div>
          </div>
        </TooltipView>
    );
  }
}

ListNodeView.propTypes = oPropTypes;

export const view = ListNodeView;
export const events = oEvents;
