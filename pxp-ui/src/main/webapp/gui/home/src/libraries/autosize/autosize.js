function getParentOverflows(el) {
    const arr = [];

    while (el && el.parentNode && el.parentNode instanceof Element) {
        if (el.parentNode.scrollTop) {
            arr.push({
                node: el.parentNode,
                scrollTop: el.parentNode.scrollTop,
            })
        }
        el = el.parentNode;
    }

    return arr;
}

function resize(ta, heightOffset) {
    if (ta.scrollHeight === 0) {
        // If the scrollHeight is 0, then the element probably has display:none or is detached from the DOM.
        return;
    }

    const overflows = getParentOverflows(ta);
    const docTop = document.documentElement && document.documentElement.scrollTop; // Needed for Mobile IE (ticket #240)

    ta.style.height = '';
    ta.style.height = (ta.scrollHeight+heightOffset)+'px';

    // used to check if an update is actually necessary on window.resize
    let clientWidth = ta.clientWidth;

    // prevents scroll-position jumping
    overflows.forEach(el => {
        el.node.scrollTop = el.scrollTop
    });

    if (docTop) {
        document.documentElement.scrollTop = docTop;
    }
}

function autoSize(ta, bForceUpdate) {
    if (ta) {
      const style = window.getComputedStyle(ta, null);
      let heightOffset;
      if (style.boxSizing === 'content-box') {
        heightOffset = -(parseFloat(style.paddingTop)+parseFloat(style.paddingBottom));
      } else {
        heightOffset = parseFloat(style.borderTopWidth)+parseFloat(style.borderBottomWidth);
      }
      // Fix when a textarea is not on document body and heightOffset is Not a Number
      if (isNaN(heightOffset)) {
        heightOffset = 0;
      }
      let styleHeight = Math.round(parseFloat(ta.scrollHeight + heightOffset));

      if (ta.offsetHeight < styleHeight || bForceUpdate) { 
        resize(ta, heightOffset);
      }
    }
}

export default autoSize;