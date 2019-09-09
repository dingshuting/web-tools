steal("can/util/can.js", "can/util/array/each.js", function(e) {
	function load(d,e){
		d.extend(e, d, {
			trigger: function(c, a, b) {
				c.trigger ? c.trigger(a, b) : d.event.trigger(a, b, c, !0)
			},
			addEvent: function(c, a) {
				d([this]).bind(c, a);
				return this
			},
			removeEvent: function(c, a) {
				d([this]).unbind(c, a);
				return this
			},
			buildFragment: function(c, a) {
				var b = d.buildFragment;
				c = [c];
				a = a || document;
				a = !a.nodeType && a[0] || a;
				a = a.ownerDocument || a;
				b = b.call(jQuery, c, a);
				return b.cacheable ? d.clone(b.fragment) : b.fragment || b
			},
			$: d,
			each: e.each
		});
		d.each(["bind",
			"unbind", "undelegate", "delegate"
		], function(c, a) {
			e[a] = function() {
				var b = this[a] ? this : d([this]);
				b[a].apply(b, arguments);
				return this
			}
		});
		d.each("append filter addClass remove data get".split(" "), function(c, a) {
			e[a] = function(b) {
				return b[a].apply(b, e.makeArray(arguments).slice(1))
			}
		});
		var f = d.cleanData;
		d.cleanData = function(c) {
			d.each(c, function(a, b) {
				b && e.trigger(b, "destroyed", [], !1)
			});
			f(c)
		};
		return e;
	}
	d=$||jQuery;
	if(!d){
		console.log("--noJquery--")
		return steal("jquery",function(d){
			return load(d,e);
		})
	}else{
		return load(d,e);
	}
	
});