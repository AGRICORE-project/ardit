import { Dataset } from '../../models/dataset';

/**
 * Check if geocoverage mutual exclusiveness is being applied
 * @param dataset: the dataset to be submitted
 * @returns: true if only one geocoverage property is set, false otherwise
 */
export function validateGeocoverage(dataset: Dataset): boolean {
    let counter = 0;
    if(dataset.continentalCoverage.length > 0) counter++;
    if(dataset.countryCoverage.length > 0) counter++;
    if(dataset.nuts1.length > 0) counter++;
    if(dataset.nuts2.length > 0) counter++;
    if(dataset.nuts3.length > 0) counter++;
    if(dataset.adm1.length > 0) counter++;
    if(dataset.adm2.length > 0) counter++;

    return ((counter <= 1) ? true: false);
}