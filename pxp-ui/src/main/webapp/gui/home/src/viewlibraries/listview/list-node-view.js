import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import ListViewModel from './model/list-view-model';
import TooltipView from './../tooltipview/tooltip-view';

const oEvents = {
  LIST_NODE_CLICKED: "list_node_clicked",
  LIST_NODE_CHECKBOX_CLICKED: "list_node_checkbox_clicked",
  LIST_NODE_DOUBLE_CLICKED: "list_node_double_clicked",
  LIST_NODE_VALUE_CHANGED: "list_node_value_changed",
  LIST_NODE_VIEW_RIGHT_BUTTON_CLICKED: "list_node_view_right_button_clicked"
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

    this.state = {
      searchText: ""
    }

    this.nodeNameInput = React.createRef();
  }

  componentDidUpdate() {
    if (this.nodeNameInput.current) {
      this.nodeNameInput.current.value = this.props.model.label;
      this.nodeNameInput.current.select();
    }
  }

  componentDidMount() {
    if (this.nodeNameInput.current) {
      this.nodeNameInput.current.value = this.props.model.label;
      this.nodeNameInput.current.select();
    }
  }

  handleClickOnListNode = (oModel, oEvent) => {
    if (!oEvent.nativeEvent.dontRaise) {
      if (oEvent.ctrlKey || oEvent.metaKey) {
        EventBus.dispatch(oEvents.LIST_NODE_CHECKBOX_CLICKED, this, oEvent, oModel);
      } else {
        EventBus.dispatch(oEvents.LIST_NODE_CLICKED, this, oEvent, oModel);
      }
    }
  }

  handleClickOnCheckBox =(oModel, oEvent)=> {
    oEvent.stopPropagation();
    EventBus.dispatch(oEvents.LIST_NODE_CHECKBOX_CLICKED, this, oEvent, oModel);
  }

  handleDoubleClickOnNodeName =(oModel, oEvent)=> {
    EventBus.dispatch(oEvents.LIST_NODE_DOUBLE_CLICKED, this, oEvent, oModel);
  }

  handleListNodeValueChanged =(oModel, oEvent)=> {
    var sNewValue = CS.trim(oEvent.target.value);
    // if (oModel.label != sNewValue) {
    var oNewModel = new ListViewModel(
        oModel.id,
        sNewValue,
        oModel.isChecked,
        oModel.isSelected,
        oModel.isEditable,
        oModel.icon,
        oModel.checkboxVisibility,
        oModel.rightIconVisibility,
        oModel.rightIconClass,
        oModel.properties
    );
    EventBus.dispatch(oEvents.LIST_NODE_VALUE_CHANGED, this, oEvent, oNewModel);
    // }
  }

  handleSingleClickOnNodeInput =(oEvent)=> {
    oEvent.stopPropagation();
  }

  handleRightIconContainerClicked = (oModel, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.LIST_NODE_VIEW_RIGHT_BUTTON_CLICKED, oModel);
  }

  handleNodeValueChanged =(oModel, oEvent)=> {
    var iKeyCode = oEvent.keyCode || oEvent.which;
    if (iKeyCode == 13) {
      this.handleListNodeValueChanged(oModel, oEvent);
    }
  }

  getNodeNameView =(oListModel)=> {
    var oListNodeNameView = null;
    var bShowId = oListModel.properties["showId"];
    var sLabel = CS.getLabelOrCode(oListModel);
    var aLabels = [sLabel];
    if(bShowId) {
      let sId = oListModel.properties["code"] ? oListModel.properties["code"] : oListModel.id;
      aLabels.push(<span className="nodeId" key={sId}>{sId}</span>);
    }

    if (oListModel.isEditable) {
      oListNodeNameView = (<input ref={this.nodeNameInput} className="listNodeViewNameInput"
                                  onBlur={this.handleListNodeValueChanged.bind(this, oListModel)}
                                  onKeyUp={this.handleNodeValueChanged.bind(this, oListModel)}
                                  onClick={this.handleSingleClickOnNodeInput}
                                  onDoubleClick={this.handleDoubleClickOnNodeName}
      />);
    } else {
      oListNodeNameView = (<div className="listNodeViewName"
                                onDoubleClick={this.handleDoubleClickOnNodeName.bind(this, oListModel)}>
        {aLabels}
      </div>);
    }

    return oListNodeNameView;
  }

  render() {

    var oListModel = this.props.model;
    var sCheckboxIconClassName = "listViewCheckBox ";
    sCheckboxIconClassName += oListModel.isChecked ? "listViewCheckBoxChecked " : "listViewCheckBoxUnChecked ";
    var sNodeClassName = oListModel.isSelected || oListModel.isChecked ? "listNodeSelected " : " ";
    var oListNodeNameView = this.getNodeNameView(oListModel);

    var oCheckbox = '';
    if (oListModel.checkboxVisibility) {
      oCheckbox = (
          <div className={sCheckboxIconClassName} onClick={this.handleClickOnCheckBox.bind(this, oListModel)}></div>);
    }

    var sNodeIcon = oListModel.properties['sXRayIconClass'];
    var oNodeIconView = null;
    var sNodeIconClassName = "nodeIconClass ";
    if(!CS.isEmpty(sNodeIcon)) {
      sNodeIconClassName += sNodeIcon;
      oNodeIconView = <div className={sNodeIconClassName}></div>;
    }

    var oRightIconContainer = '';
    if (oListModel.rightIconVisibility) {
      oRightIconContainer = (<div className={"rightIconContainer "+ oListModel.rightIconClass}
                                  onClick={this.handleRightIconContainerClicked.bind(this, oListModel)}></div>);
    }

    var bShowId = oListModel.properties["showId"];
    var sLabel = CS.getLabelOrCode(oListModel);
    var aLabels = [sLabel];
    if(bShowId) {
      let sId = oListModel.properties["code"] ? oListModel.properties["code"] : oListModel.id;
      aLabels.push(<span className="tooltipNodeId" key={sId}>{sId}</span>);
    }

    return (
        <TooltipView label={aLabels}>
          <div className={"listNodeViewContainer " + sNodeClassName}
               onClick={this.handleClickOnListNode.bind(this, oListModel)}>
            {oCheckbox}
            {oListNodeNameView}
            {oNodeIconView}
            {oRightIconContainer}

          </div>
        </TooltipView>
    );
  }
}

ListNodeView.propTypes = oPropTypes;

export const view = ListNodeView;
export const events = oEvents;
