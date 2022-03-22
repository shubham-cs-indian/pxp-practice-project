import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import MaterialTooltip from '@material-ui/core/Tooltip';
import { withStyles } from '@material-ui/core/styles';
import Fade from '@material-ui/core/Fade';
import ViewUtils from "../utils/view-library-utils";

let oTooltipLeftStyles = {
  margin: '0',
  right: '-1em'
};

let oTooltipRightStyles = {
  margin: '0',
  left: '1em'
};

if(ViewUtils.isFirefox()){
  oTooltipLeftStyles.right = '-1.5em';
  oTooltipRightStyles.left = '-1.5em'
}

const styles = theme => ({
  lightTooltip: {
    background: theme.palette.common.white,
    color: theme.palette.text.primary,
    boxShadow: theme.shadows[1],
    fontSize: 11,
  },

  arrowPopper: {
    '&[x-placement*="bottom"]': {
      top: 0
    },

    '&[x-placement*="right"]': {
      left: 0
    },

    '&[x-placement*="left"]': {
      left: 0
    },

    '&[x-placement*="top"]': {
      top: 0
    },


    '&[x-placement*="bottom"] $arrowArrow': {
      top: 0,
      left: 0,
      marginTop: '-0.9em',
      width: '3em',
      height: '1em',
      '&::before': {
        borderWidth: '0 1em 1em 1em',
        borderColor: `transparent transparent ${theme.palette.grey[700]} transparent`,
      },
    },
    '&[x-placement*="top"] $arrowArrow': {
      bottom: 0,
      left: 0,
      marginBottom: '-0.9em',
      width: '3em',
      height: '1em',
      '&::before': {
        borderWidth: '1em 1em 0 1em',
        borderColor: `${theme.palette.grey[700]} transparent transparent transparent`,
      },
    },
    '&[x-placement*="right"] $arrowArrow': {
      left: 0,
      marginLeft: '-0.9em',
      height: '3em',
      width: '1em',
      paddingTop: '0.4em',
      '&::before': {
        borderWidth: '1em 1em 1em 0',
        borderColor: `transparent ${theme.palette.grey[700]} transparent transparent`,
      },
    },
    '&[x-placement*="left"] $arrowArrow': {
      right: 0,
      marginRight: '-0.9em',
      height: '3em',
      width: '1em',
      paddingTop: '0.4em',
      '&::before': {
        borderWidth: '1em 0 1em 1em',
        borderColor: `transparent transparent transparent ${theme.palette.grey[700]}`,
      },
    },
  },
  arrowArrow: {
    position: 'absolute',
    fontSize: '0.6em',
    width: '3em',
    height: '3em',
    '&::before': {
      content: '""',
      margin: 'auto',
      display: 'block',
      width: 0,
      height: 0,
      borderStyle: 'solid',
    },
  },
  button: {
    margin: theme.spacing(1),
  },
  tooltip: {
    fontSize: '0.8em',
    position: 'relative',
    wordBreak: 'break-all',
  },
  tooltipPlacementBottom: {
    margin: '8px 0'
  },

  tooltipPlacementRight: oTooltipRightStyles,

  tooltipPlacementLeft: oTooltipLeftStyles,

  tooltipPlacementTop: {
    margin: '8px 0'
  },
});

const oAnonymousPropTypes = {
  placement: ReactPropTypes.string,
  label: ReactPropTypes.oneOfType([ReactPropTypes.string, ReactPropTypes.object, ReactPropTypes.array]).isRequired,
  delay: ReactPropTypes.number,
  getTriggerEvent: ReactPropTypes.func.isRequired
};
/**
 * @class Anonymous - Use for display tooltips.
 * @memberOf Views
 * @property {string} [placement] - Pass place for where to display tooltip.
 * @property {custom} label - Pass label of tooltip.
 * @property {number} [delay] - Pass delay time for display tooltip.
 * @property {function} [getTriggerEvent] - trigger function to trigger tooltip [click, touch, hover etc..].
 */

class BaseTooltipView extends React.Component {

  static propTypes = oAnonymousPropTypes;

  constructor(props) {
    super(props);

    this.state = {
      arrowRef: null,
    };

    this.handleArrowRef = node => {
      this.setState({
        arrowRef: node,
      });
    };

  }

  render () {

    const sPlacement = this.props.placement || "bottom";
    const fFetTriggerEvent = this.props.getTriggerEvent();
    const {classes} = this.props;
    let oProps = {};
    if(CS.isBoolean(this.props.open)){
      oProps.open = this.props.open;
      oProps.close = this.props.close;
    }

    return (
        <MaterialTooltip
            placement={sPlacement}
            {...fFetTriggerEvent}
            enterDelay={200}
            TransitionComponent={Fade}
            TransitionProps={{ timeout: 600 }}
            title={
              <React.Fragment>
                {this.props.label}
                <span className={classes.arrowArrow} ref={this.handleArrowRef}/>
              </React.Fragment>
            }
            classes={{popper: classes.arrowPopper, tooltip: classes.tooltip, tooltipPlacementBottom: classes.tooltipPlacementBottom,
              tooltipPlacementRight: classes.tooltipPlacementRight, tooltipPlacementLeft: classes.tooltipPlacementLeft, tooltipPlacementTop: classes.tooltipPlacementTop}}
            PopperProps={{
              popperOptions: {
                modifiers: {
                  arrow: {
                    enabled: Boolean(this.state.arrowRef),
                    element: this.state.arrowRef,
                  },
                },
              },
            }}
            {...oProps}
        >
          {this.props.children}
        </MaterialTooltip>
    )
  }
}

export default withStyles(styles)(BaseTooltipView);

