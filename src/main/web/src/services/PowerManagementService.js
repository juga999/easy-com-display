import {AbstractService} from '@/services/ServiceUtils.js'

const SETTINGS_API_ROOT = '/api/ecd/settings/'

class PowerManagementService extends AbstractService {

    getTvTimes() {
        return this.get(SETTINGS_API_ROOT + 'tv-times').then((response) => {
            let result = response.body;
            return result;
        }, (response) => {
            console.log(response);
            return null;
        })
    }

    setTvTimes(tvTimes) {
        return this.postJson(SETTINGS_API_ROOT + 'tv-times', tvTimes).then((response) => {
            return true;
        }, (response) => {
            console.log(response);
            return false;
        });
    }

}

export const powerManagementService = new PowerManagementService();
