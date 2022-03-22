
import React from 'react';
import ReactPropTypes from 'prop-types';
import CS from '../cs';
import CreateClass from 'create-react-class';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';

/*
* In some library's still using React.PropTypes to define prop types.
* There fore we assigning ReactPropTypes to React.PropTypes
* */

React.PropTypes = ReactPropTypes;

var fOriginalCreateClass = CreateClass;

var fFunctionCreator = function (fFunctionToExecute, oFailureReturn) {
  var sDisplayName = this.displayName || '';
  if(fFunctionToExecute) {
    if(CS.isFunction(fFunctionToExecute)) {
      return function () {
        try {
          return fFunctionToExecute.apply(this, arguments);
        }
        catch (oException2) {
          sDisplayName && console.error(sDisplayName);
          console.error(oException2);
          return oFailureReturn;
        }
      };
    }
    else {
      console.error("IS_NOT_A_FUNCTION ", sDisplayName);
      console.error(fFunctionToExecute);
    }
  }
};

var createClass = function (oObject) {

  try {

    var fFunctionCreatorCaller = fFunctionCreator.bind(oObject);

    oObject.render = fFunctionCreatorCaller(oObject.render, null);
    oObject.getDefaultProps = fFunctionCreatorCaller(oObject.getDefaultProps, {});
    oObject.getInitialState = fFunctionCreatorCaller(oObject.getInitialState, {});
    oObject.componentWillMount = fFunctionCreatorCaller(oObject.componentWillMount, undefined);
    oObject.componentWillUpdate = fFunctionCreatorCaller(oObject.componentWillUpdate, undefined);
    oObject.componentWillReceiveProps = fFunctionCreatorCaller(oObject.componentWillReceiveProps, undefined);
    oObject.componentWillUnmount = fFunctionCreatorCaller(oObject.componentWillUnmount, undefined);
    oObject.shouldComponentUpdate = fFunctionCreatorCaller(oObject.shouldComponentUpdate, false);

    oObject.componentDidMount = fFunctionCreatorCaller(oObject.componentDidMount, undefined);
    oObject.componentDidUpdate = fFunctionCreatorCaller(oObject.componentDidUpdate, undefined);

    if(oObject.componentWillMount === undefined) {
      delete oObject.componentWillMount;
    }
    if(oObject.componentWillUpdate === undefined) {
      delete oObject.componentWillUpdate;
    }
    if(oObject.componentWillReceiveProps === undefined) {
      delete oObject.componentWillReceiveProps;
    }
    if(oObject.shouldComponentUpdate === undefined) {
      delete oObject.shouldComponentUpdate;
    }
    if(oObject.componentDidMount === undefined) {
      delete oObject.componentDidMount;
    }
    if(oObject.componentDidUpdate === undefined) {
      delete oObject.componentDidUpdate;
    }
    if(oObject.componentWillUnmount === undefined) {
      delete oObject.componentWillUnmount;
    }

    return fOriginalCreateClass.apply(this, arguments);
  }
  catch (oException) {
    ExceptionLogger.log(oException);
    oObject.displayName && ExceptionLogger.error(oObject.displayName);
    return <noscript/>
  }
};

/**
 *
 * @param View
 * @returns {{propTypes, new(*=): {render: (function()), componentDidCatch: (function(*=, *=))}}}
 * @constructor
 * @deprecated
 */
let ErrorBoundary = function (View) {

  if(!View) {
    throw new Error("UNDEFINED_VIEW_PASSED");
  }

  return class extends React.Component {

    static propTypes = View.propTypes;

    constructor (props) {
      super(props);
      this.className = View.name;
      this.state = { hasError: false };
    }

    /*componentWillReceiveProps() {
      this.setState({ hasError: false});
    }*/

    componentDidCatch(error, info) {
      // Display fallback UI
      console.warn("View: " + this.className);
      console.info(info);
      console.error(error);
      // this.setState({ hasError: true });
    }

    render () {
      /*if (this.state.hasError) {
        return null;
      }*/
      return <View {...this.props}/>;
    }
  }
};


Object.defineProperty (React, 'createClass', {
      value : createClass,
      writable : false
    }
);

/**
 * @deprecated Property getSafeComponent instead use // @CS.SafeComponent
 */
Object.defineProperty (React, 'getSafeComponent', {
      value : ErrorBoundary,
      writable : false
    }
);

/**
 * @deprecated Property getSafeComponent instead use // @CS.SafeComponent
 */
Object.defineProperty (React, 'ErrorBoundary', {
      value : ErrorBoundary,
      writable : false
    }
);
