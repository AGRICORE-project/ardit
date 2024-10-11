/**
 * Simulates a download with given params
 * @param jsonObj: given json object
 */
export function simulateDownload(jsonObj: string): void {

    const downloader = document.createElement('a');

    downloader.setAttribute('href', jsonObj);
    downloader.setAttribute('download', jsonObj + '.json'); //Download attribute, sets de file name
    downloader.click()
}