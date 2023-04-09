
/**
 * jquery.LavaLamp v1.3.4b2 - light up your menu with fluid, jQuery powered animations.
 *
 * Requires jQuery v1.2.3 or better from http://jquery.com
 * Tested on jQuery 1.4, 1.3.2 and 1.2.6
 *
 * http://nixboxdesigns.com/projects/jquery-lavalamp/
 *
 * Copyright (c) 2008, 2009, 2010 Jolyon Terwilliger, jolyon@nixbox.com
 * Source code Copyright (c) 2008, 2009, 2010
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 * credits to Guillermo Rauch and Ganeshji Marwaha (gmarwaha.com) for previous editions
 *
 * Version: 1.0 - adapted for jQuery 1.2.x series
 * Version: 1.1 - added linum parameter
 * Version: 1.2 - modified to support vertical resizing of elements
 * Version: 1.3 - enhanced automatic <li> item hi-lighting - will attempt to
 *					lock onto li > a element with href closest to selected
 *					window.location
 *			 	- click always returns 'true' by default, for standard link follow through.
 *
 * Version: 1.3.1 - verified for use with jQuery 1.3 - should still work with 1.2.x series
 *				- changed linum parameter to startItem for clarity
 *				- improved slide-in accuracy for .back elements with borders
 *				- changed .current class to .selectedLava for clarity and improved
 *					support
 *				- appended 'Lava' to all internal class names to avoid conflicts
 *				- fixed bug applying selectedLava class to elements with matching
 *					location.hash
 *				- now supports jquery.compat plugin for cross-library support
 *				- performance improvements
 *				- added new options:
 *				autoReturn: true - if set to false, hover will not return to last selected
 *									item upon list mouseout.
 *				returnDelay: 0 - if set, will delay auto-return feature specified # of
 *									milliseconds.
 *				setOnClick: true - if set to false, hover will return to default element
 *									regardless of click event.
 *				homeLeft: 0, homeTop: 0 - if either set to non zero value, absolute
 *									positioned li element with class .homeLava is 
 *									prepended to list for homing feature.
 *				homeWidth: 0, homeHeight: 0 - if set, are used for creation of li.homeLava
 *									element.
 *				returnHome: false - if set along with homeLeft or homeTop, lavalamp hover
 *									will always return to li.home after click.
 *
 * Version: 1.3.2 - fixed: stray $ references inside the plugin to work with
 *					jQuery.noConflict() properly - thanks Colin.
 *
 * Version: 1.3.3 - fixed: added closure with null passed argument for move() command in
 * 					returnDelay to fix errors some were seeing - thanks to Michel and 
 *					Richard for noticing this.
 *
 *					fixed: changed mouseover/out events to mouseenter/leave to fix jerky
 *					animation problem when using excessive margins instead of padding.  
 *					Thanks to Thomas for the solution and Chris for demonstrating the problem.
 *					* requires jQuery 1.3 or better
 *
 *					enhanced: added 'noLava' class detection to prevent LavaLamp effect
 *					application to LI elements with this class. This feature allows you to
 *					create submenus - for details, see examples at
 *					http://nixboxdesigns.com/demos/jquery-lavalamp-demos.html
 *
 *					enhanced: modified to better automatically find default location for 
 *					relative links. Thanks to Harold for testing and finding this bug.
 *
 * Version: 1.3.4 - major overhaul on practically everything:
 *					enhanced: added target and autoResize options - see below.
 *					enhanced: better automatic default item selection and URI resolution,
 *					better support for returnHome and returnDelay, refined internal variable
 *					usage and test to be as lean as possible
 *					fixed: backLava hover element now exactly covers the destination LI dimensions.
 *					fixed: changed use of mouseleave/mouseenter to bind events so will work with
 *							jQuery 1.2.2 onward.
 *					enhanced: behaves more like a plugin should and now automatically adds proper
 * 							position CSS tags to the backLava element and parent container
 *							if absent. 
 *
 * Examples and usage:
 *
 * The HTML markup used to build the menu can be as simple as...
 *
 *       <ul class="lavaLamp">
 *           <li><a href="#">Phone Home</a></li>
 *           <li><a href="#">Make Contact</a></li>
 *           <li><a href="#">Board Ship</a></li>
 *           <li><a href="#">Fly to Venus</a></li>
 *       </ul>
 *
 * Additional Styles must be added to make the LavaLamp perform properly, to wit:
 *
 * <style>
 * ul.lavaLamp {
 *   padding:5px;  // use some minimal padding to account for sloppy mouse movements
 * }
 * ul.lavaLamp li.backLava {
 *   z-index:3;   // must be less than z-index of A tags within the LI elements
 * }
 * ul.lavaLamp li a {
 *  display:block;  // helps with positioning the link within the LI element
 *  z-index:10;     // or must be higher than li.backLava z-index
 * }
 * </style>
 *
 * Once you have included the basic styles above, you will need to include 
 * the jQuery library, easing plugin (optional) and the this LavaLamp plugin.
 *
 * jQuery Easing Library 1.3 available here:  http://plugins.jquery.com/project/Easing
 * 
 * Example LavaLamp initializing statement:
 * jQuery(function() { jQuery("ul.lavaLamp").lavaLamp({ fx: "easeOutBack", speed: 700}) });
 * finds all UL elements in the document with the class of 'lavaLamp' and attaches the 
 * LavaLamp plugin using an easing library fx of OutBack and an animate speed of 
 * 700 milliseconds or 7/10ths of a second.
 *
 *
 * List of Parameters
 *
 * @param target - default: 'li' 
 * valid selector for target elements to receive hover effect
 *
 * Example:
 * jQuery("div#article").lavaLamp({ target:'p' });
 * assigns all p elements under div#article to receive lavaLamp hover events
 *
 * @param fx - default: 'swing'
 * selects the easing formula for the animation - requires the jQuery Easing library 
 * to be loaded for additional effects
 * 
 * Example:
 * jQuery("ul.lavaLamp").lavaLamp({ fx: "easeOutElastic" });
 * animates the backLava element using the OutElastic formula
 * 
 * @param speed - default: 500
 * sets animation speed in milliseconds
 * 
 * Example:
 * jQuery("ul.lavaLamp").lavaLamp({ speed: 1000 });
 * sets the animation speed to one second.
 * 
 * @param click - default: function() { return true; }
 * Callback to be executed when the menu item is clicked. The 'event' object and source target
 * DOM element will be passed in as arguments so you can use them in your function.
 * 
 * Example:
 * jQuery("ul.lavaLamp").lavaLamp({ click: function(event, menuItem) {
 *		alert(event+el);
 *		return false;
 * } });
 *
 * causes the browser to display an alert message of the variables passed and 
 * return false aborts any other click events on child items, including not 