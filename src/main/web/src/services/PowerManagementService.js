import Vue from 'vue'
import VueResource from 'vue-resource'

Vue.use(VueResource);

const SETTINGS_API_ROOT = '/api/ecd/settings/'

export default {

    getTvTimes() {
        return Vue.http.get(SETTINGS_API_ROOT + 'tv-times').then((response) => {
            let result = response.body;
            return result;
        }, (response) => {
            console.log(response);
            return null;
        })
    },

    setTvTimes(wakeupTime, sleepTime) {
        let formData = new FormData();

        formData.append('tvTimes', JSON.stringify({
            wakeupTime: wakeupTime || '',
            sleepTime: sleepTime || ''
        }));

        return Vue.http.post(SETTINGS_API_ROOT + 'tv-times', formData).then((response) => {
            return true;
        }, (response) => {
            console.log(response);
            return false;
        });
    }

}
