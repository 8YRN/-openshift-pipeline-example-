/**
* Returns the week number for this date. dowOffset is the day of week the week
* "starts" on for your locale - it can be from 0 to 6. If dowOffset is 1 (Monday),
* the week returned is the ISO 8601 week number.
* @param long date in msecs
* @param int dowOffset
* @return int
*/
function getWeek(dateLong, dowOffset) {

	date = new Date(dateLong);
	/*
	 * getWeek() was developed by Nick Baicoianu at MeanFreePath: http://www.meanfreepath.com
	 * modified by Kamil
	 */

	dowOffset = typeof(dowOffset) == 'int' ? dowOffset : 0; //default dowOffset to zero
	var newYear = new Date(date.getFullYear(),0,1);
	var day = newYear.getDay() - dowOffset; //the day of week the year begins on
	day = (day >= 