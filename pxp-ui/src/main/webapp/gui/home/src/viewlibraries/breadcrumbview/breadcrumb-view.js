import React from 'react';
import ReactPropTypes from 'prop-types';

import {view as ContextMenuViewNew} from '../contextmenuwithsearchview/context-menu-view-new';

const oPropTypes = {
  items: ReactPropTypes.array,
  onClick: ReactPropTypes.func
};

const SeparatorView = () => (<div className='breadcrumbItemSeparator'/>);

function BreadcrumbItemView (props) {
  let label = props.oItem.label,
      onItemClick = props.oItem.onItemClick,
      sId = props.oItem.id;
  return React.createElement(
      'div',
      {
        key: sId,
        className: 'breadcrumbItem',
        onClick: function onClick () {
          onItemClick(props.oItem, props.isLastElement);
        },
        title: label
      },
      label
  );
}

function BreadcrumbView (props) {
  let items = props.items,
      onClick = props.onClick;

  let MAX_ITEMS_TO_SHOW = 4;
  let TOTAL_ITEMS = items.length;
  let IS_POPOVER_NEEDED = TOTAL_ITEMS > MAX_ITEMS_TO_SHOW;
  let itemDOMS = [];
  let aItemsToShow = [];

  let onItemClick = function onItemClick (oItem, bIsLAstElement) {
    if (onClick) {
      if (onClick instanceof Function) {
        onClick(oItem, bIsLAstElement);
      } else {
        throw Error("onClick is not a valid function");
      }
    }
  };

  let handleListItemClicked = function handleListItemClicked (bIsLAstElement, oItem) {
    oItem.onItemClick(oItem, bIsLAstElement)
  };

  items.forEach(function (oItem, iIndex) {
    oItem.onItemClick = onItemClick;
    let isLastElement = iIndex === items.length - 1;

    let oItemDOM = React.createElement(BreadcrumbItemView, {
      oItem, isLastElement, key: "biv" + iIndex,
    });

    if (IS_POPOVER_NEEDED && iIndex > 0 && iIndex < TOTAL_ITEMS - 2) {

      aItemsToShow.push(oItem);
      if (iIndex === 1) {
        let oMoreContainer = React.createElement(
            'div',
            {key: "more", className: 'breadcrumbItemDropdownButton', /*onClick: handleClick*/},
            ' ... '
        );

        itemDOMS.push(<ContextMenuViewNew key={"breadcrumbCMItem"}
                                          showSearch={false}
                                          contextMenuViewModel={aItemsToShow}
                                          anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
                                          targetOrigin={{horizontal: 'left', vertical: 'bottom'}}
                                          onClickHandler={handleListItemClicked.bind(this, isLastElement)}
                                          children={oMoreContainer}/>);
        itemDOMS.push(<SeparatorView key={iIndex} />);
      }
      return;
    } else {
      itemDOMS.push(oItemDOM);
    }

    if (iIndex < TOTAL_ITEMS - 1) {
      itemDOMS.push(<SeparatorView key={iIndex} />);
    }
  });

  return <div className='breadcrumbContainer'>{itemDOMS}</div>;
}

BreadcrumbView.propTypes = oPropTypes;

export default BreadcrumbView;
