/**
 * Adds a day to the temporal extent string and generates a complete Date
 * @param tmpExtent: string with format yyyy-mm
 */
export function formatTmpExtentToDate(tmpExtent: string): Date {

    let dateString = tmpExtent.concat("-01"); // Add the 1st day of the month
    return new Date(dateString); // Generate a Date from the string 
}

/**
 * Creates a string with the year and the month, deleting the day
 * @param tmpExtent: date with format yyyy-mm-dd
 */
export function formatTmpExtentToString(tmpExtent: Date): String {

    let tmpExtentDate = new Date(String(tmpExtent));

    // Month format: getMonth() starts counting from 0
    // Use 2 digit format, adding a the 0
    let month = ("0" + (tmpExtentDate.getMonth() + 1)).slice(-2);

    return tmpExtentDate.getFullYear() + "-" + month;
}