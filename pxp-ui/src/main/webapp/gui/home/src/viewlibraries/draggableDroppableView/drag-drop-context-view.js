import CS from "../../libraries/cs";
import React from "react";
import ReactPropTypes from "prop-types";
import EventBus from "../../libraries/eventdispatcher/EventDispatcher";
import {view as DroppableView} from "./droppable-view";
import {DragDropContext} from 'react-beautiful-dnd';

const oEvents = {
  DRAG_DROP_CONTEXT_VIEW_PROPERTY_SHUFFLED: "drag_drop_context_view_property_shuffled"
};

const oPropTypes = {
  listData: ReactPropTypes.array,
  context: ReactPropTypes.string,
  onDragStart: ReactPropTypes.func,
  onDragEnd: ReactPropTypes.func,
  showDraggableIcon: ReactPropTypes.bool,
  enableMultiDrag: ReactPropTypes.bool,
  onRowSelect: ReactPropTypes.func,
};

// @CS.SafeComponent
class DragDropContextView extends React.Component {
  static propTypes = oPropTypes;

  constructor (props) {
    super(props);

    this.state = {
      selectedPropertiesForDrag: [],
      selectedPropertiesListId: ""
    }
  }

  onRowSelect = (sListId, sSelectedItemId, bIsMultiSelect) => {
    let aList = this.state.selectedPropertiesListId !== sListId ? [] : this.state.selectedPropertiesForDrag;
    if (CS.includes(aList, sSelectedItemId)) {
      /**Remove selected item if already present in list*/
      if (bIsMultiSelect) {
        CS.remove(aList, function (sListId) {
          return sListId === sSelectedItemId;
        });
      } else {
        aList = [];
      }
    } else {
      /**Add selected item in list*/
      bIsMultiSelect ? aList.push(sSelectedItemId) : (aList = [sSelectedItemId]);
    }
    this.setState({
      selectedPropertiesListId: sListId,
      selectedPropertiesForDrag: aList
    });
  };

  onDragEnd = result => {
    const {source, destination, draggableId} = result;
    let sContext = this.props.context;
    if (!destination)
      return;
    if (destination.droppableId === source.droppableId && source.index === destination.index)
      return;

    let sSelectedPropertiesForDrag = this.state.selectedPropertiesForDrag;
    let aDraggableIds = (CS.isNotEmpty(sSelectedPropertiesForDrag) && CS.includes(sSelectedPropertiesForDrag, draggableId)) ? this.state.selectedPropertiesForDrag : [draggableId];
    if (this.props.onDragEnd) {
      this.props.onDragEnd(source, destination, aDraggableIds);
    } else {
      EventBus.dispatch(oEvents.DRAG_DROP_CONTEXT_VIEW_PROPERTY_SHUFFLED, sContext, source, destination, draggableId);
    }

    if (CS.isNotEmpty(this.state.selectedPropertiesForDrag)) {
      this.setState({
        selectedPropertiesListId: "",
        selectedPropertiesForDrag: []
      });
    }
  };

  onDragStart = result => {
    if (this.props.onDragStart) {
      this.props.onDragStart(result);
    }
    let sDraggableId = result.draggableId;
    let aSelectedPropertiesForDrag = this.state.selectedPropertiesForDrag;
    if (!CS.includes(aSelectedPropertiesForDrag, sDraggableId)) {
      this.setState({
        selectedPropertiesListId: "",
        selectedPropertiesForDrag: []
      });
    }
  };

  getHeaderView = (sHeaderLabel) => {
    if (CS.isEmpty(sHeaderLabel)) {
      return null;
    }
    return (
        <div className="droppableContainerHeader">
          <div className="headerLabel">{sHeaderLabel}</div>
        </div>
    );
  };

  getDroppableListDOM = (oListData) => {
    return (
        <div className={"dragDropContextViewWrapper withHeader"}>
          {this.getHeaderView(oListData.headerLabel)}
          <div className={"droppableListContainer"}>
              <DroppableView sDroppableId={oListData.droppableId}
                             aItems={oListData.items}
                             oLoadMoreDOM={oListData.loadMoreDOM}
                             oNothingFoundMessageDOM={oListData.nothingFoundMessageDOM}
                             fOnRowSelect={this.onRowSelect.bind(this, oListData.droppableId)}
                             aSelectedPropertiesForDrag={this.state.selectedPropertiesForDrag}
                             bShowDraggableIcon={this.props.showDraggableIcon}
                             bEnableMultiDrag={this.props.enableMultiDrag}
                             bIsDropDisable={oListData.isDropDisabled}
              />
            </div>
        </div>
    );
  };

  getDragDropContextView = () => {
    let _this = this;
    let aDroppableListData = this.props.listData;
    let aDroppableListDOM = [];
    CS.forEach(aDroppableListData, function (oListData) {
      aDroppableListDOM.push(_this.getDroppableListDOM(oListData));
    });

    return (
        <DragDropContext onDragEnd={this.onDragEnd} onDragStart={this.onDragStart}>
          {aDroppableListDOM}
        </DragDropContext>
    )
  };

  render () {
    return this.getDragDropContextView();
  }
}

export const view = DragDropContextView;
export const events = oEvents;
