steal("can/util","can/construct",function(d){var r=function(a,c,b){d.bind.call(a,c,b);return function(){d.unbind.call(a,c,b)}},m=d.isFunction,t=d.extend,n=d.each,u=[].slice,p=/\{([^\}]+)\}/g,v=d.getObject("$.event.special",[d])||{},w=function(a,c,b,e){d.delegate.call(a,c,b,e);return function(){d.undelegate.call(a,c,b,e)}},q=function(a,c,b,e){return e?w(a,d.trim(e),c,b):r(a,c,b)},k,x=d.Control=d.Construct({setup:function(){d.Construct.setup.apply(this,arguments);if(d.Control){var a;this.actions={};
for(a in this.prototype)this._isAction(a)&&(this.actions[a]=this._action(a))}},_shifter:function(a,c){var b="string"==typeof c?a[c]:c;m(b)||(b=a[b]);return function(){a.called=c;return b.apply(a,[this.nodeName?d.$(this):this].concat(u.call(arguments,0)))}},_isAction:function(a){var c=this.prototype[a],b=typeof c;return"constructor"!==a&&("function"==b||"string"==b&&m(this.prototype[c]))&&!!(v[a]||l[a]||/[^\w]/.test(a))},_action:function(a,c){p.lastIndex=0;if(c||!p.test(a)){var b=c?d.sub(a,[c,window]):
a;if(!b)return null;var e=d.isArray(b),h=e?b[1]:b,f=h.split(/\s+/g),g=f.pop();return{processor:l[g]||k,parts:[h,f.join(" "),g],delegate:e?b[0]:void 0}}},processors:{},defaults:{}},{setup:function(a,c){var b=this.constructor,e=b.pluginName||b._fullName;this.element=d.$(a);e&&"can_control"!==e&&this.element.addClass(e);(e=d.data(this.element,"controls"))||d.data(this.element,"controls",e=[]);e.push(this);this.options=t({},b.defaults,c);this.on();return[this.element,this.options]},on:function(a,c,b,
e){if(!a){this.off();a=this.constructor;c=this._bindings;b=a.actions;e=this.element;var h=d.Control._shifter(this,"destroy"),f,g;for(f in b)b.hasOwnProperty(f)&&(g=b[f]||a._action(f,this.options))&&c.push(g.processor(g.delegate||e,g.parts[2],g.parts[1],f,this));d.bind.call(e,"destroyed",h);c.push(function(a){d.unbind.call(a,"destroyed",h)});return c.length}"string"==typeof a&&(e=b,b=c,c=a,a=this.element);void 0===e&&(e=b,b=c,c=null);"string"==typeof e&&(e=d.Control._shifter(this,e));this._bindings.push(q(a,
b,e,c));return this._bindings.length},off:function(){var a=this.element[0];n(this._bindings||[],function(c){c(a)});this._bindings=[]},destroy:function(){if(null===this.element)steal.dev.warn("Control.js - Control already destroyed");else{var a=this.constructor,a=a.pluginName||a._fullName;this.off();a&&"can_control"!==a&&this.element.removeClass(a);a=d.data(this.element,"controls");a.splice(d.inArray(this,a),1);d.trigger(this,"destroyed");this.element=null}}}),l=d.Control.processors;k=function(a,c,
b,e,h){return q(a,c,d.Control._shifter(h,e),b)};n("change click contextmenu dblclick keydown keyup keypress mousedown mousemove mouseout mouseover mouseup reset resize scroll select submit focusin focusout mouseenter mouseleave touchstart touchmove touchcancel touchend touchleave".split(" "),function(a){l[a]=k});return x});