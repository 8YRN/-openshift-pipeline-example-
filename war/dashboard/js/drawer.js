/**
 * @name       jQuery.drawer
 * @version    1.00, 2008/05/01
 * @author     inamorix <inamorix@metatype.jp>
 * @copyright  Copyright (c) 2008, metatype.jp.
 * @license    The MIT-style license.
 */



(function ($, loaded) {
$.extend({ drawer: {
	init: function () {
		this.self   = null;
		this.closer = '<div id="drw_close"><a href="#" class="drw_close" title="close">Hide</a></div>';
		this.loader = '<div id="drw_loader">Loading...</div>';
		this.setTabs();
		$('#drw').html('<div id="drw_content"></div>');
	},
	
	
	
	setTabs: function () {
		var tabs  = $('#drw_tabs li');
		var w     = Math.floor(100 / ta