import React, {Component} from 'react';

export default function (View)  {

  if(!View) {
    throw new Error("UNDEFINED_VIEW_PASSED");
  }

  class CSSafe extends Component {

    static propTypes = View.propTypes;

    constructor (props) {
      super(props);
      this.className = View.name;
      // this.state = { hasError: false };
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

      const {forwardedRef} = this.props;
      return <View {...this.props} ref={forwardedRef}/>;
    }
  }

  CSSafe.displayName = `CSSafe (${getDisplayName(View)})`;


  // Note the second param "ref" provided by React.forwardRef.
  // We can pass it along to LogProps as a regular prop, e.g. "forwardedRef"
  // And it can then be attached to the Component.
  return React.forwardRef((props, ref) => {
    return <CSSafe {...props} forwardedRef={ref} />;
  });

};


function getDisplayName(WrappedComponent) {
  return WrappedComponent.displayName || WrappedComponent.name || 'Component';
}
