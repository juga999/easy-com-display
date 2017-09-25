import {AbstractService} from '@/services/ServiceUtils.js'

const SETTINGS_API_ROOT = '/api/ecd/settings/'

class LocationService extends AbstractService {

    getLocation() {
        return this.get(SETTINGS_API_ROOT + 'location').then((response) => {
            let result = response.body;
            return result;
        }, (response) => {
            console.log(response);
            return null;
        })
    }

    setLocation(location) {
        return this.postJson(SETTINGS_API_ROOT + 'location', location).then((response) => {
            return true;
        }, (response) => {
            console.log(response);
            return false;
        });
    }

}

export const locationService = new LocationService();
