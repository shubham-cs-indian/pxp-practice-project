import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import {Droppable, Draggable} from 'react-beautiful-dnd';

const oEvents = {};

const oPropTypes = {
  aItems: ReactPropTypes.array,
  sDroppableId: ReactPropTypes.string,
  oEmptyMessage: ReactPropTypes.object,
  oLoadMoreDOM: ReactPropTypes.object,
  fOnRowSelect: ReactPropTypes.func,
  aSelectedPropertiesForDrag: ReactPropTypes.array,
  bIsDropDisable: ReactPropTypes.bool,
  bShowDraggableIcon: ReactPropTypes.bool,
  oNothingFoundMessageDOM: ReactPropTypes.object,
  bEnableMultiDrag: ReactPropTypes.bool
};

function DroppableView (oProps) {

  let onRowClick = (oProvided, oSnapshot, sListItemId, oEvent) => {
    let bIsMultiSelect = oProps.bEnableMultiDrag && oEvent.ctrlKey;
    oProps.fOnRowSelect(sListItemId, bIsMultiSelect);
  };

  let getDraggableIconDOM = (
      oProps.bShowDraggableIcon ? <div className="dragableIcon"></div> : null
  );

  let getListItemView = () => {
    let aListItems = [];
    CS.forEach(oProps.aItems, function (oListItem, iIndex) {
      let sId = oListItem.id;
      let sClassName = "draggableWrapper ";
      sClassName += CS.includes(oProps.aSelectedPropertiesForDrag, sId) ? " selected" : "";
      sClassName += !!oListItem.shouldHighlight ? " highlight" : "";
      aListItems.push(
            <Draggable
                key={sId}
                draggableId={sId}
                index={iIndex}>
              {(provided, snapshot) => {
                let aSelectedPropertiesLength = oProps.aSelectedPropertiesForDrag.length;
                let bShouldShowSelection = snapshot.isDragging && aSelectedPropertiesLength > 1;
                return (<div
                    className={sClassName}
                    ref={provided.innerRef}
                    {...provided.draggableProps}
                    {...provided.dragHandleProps}
                    onClick={(event) =>
                        onRowClick(provided, snapshot, sId, event)
                    }
                >
                  {oListItem.label || oListItem.component}
                  {getDraggableIconDOM}
                  {bShouldShowSelection && <div className={"selectionCount"}>
                    {aSelectedPropertiesLength}
                  </div>}
                </div>)
              }}
            </Draggable>
      );
    });
    return aListItems;
  };

  return (
      <Droppable droppableId={oProps.sDroppableId} isDropDisabled={oProps.isDropDisabled}
                 ignoreContainerClipping={true}>
        {(provided) => (
            <div
                {...provided.droppableProps}
                ref={provided.innerRef}
                className={"droppableContainer"}>
              {getListItemView()}
              {provided.placeholder}
              {oProps.oNothingFoundMessageDOM}
              {oProps.oLoadMoreDOM}
            </div>

        )}
      </Droppable>
  );
}


DroppableView.propTypes = oPropTypes;

export var view = CS.SafeComponent(DroppableView);
export var events = oEvents;
