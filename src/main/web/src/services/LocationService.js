import Vue from 'vue'
import VueResource from 'vue-resource'

Vue.use(VueResource);

const SETTINGS_API_ROOT = '/api/ecd/settings/'

export default {

    getLocation() {
        return Vue.http.get(SETTINGS_API_ROOT + 'location').then((response) => {
            let result = response.body;
            return result;
        }, (response) => {
            console.log(response);
            return null;
        })
    },

    setLocation(locationName, forecastUrl) {
        let formData = new FormData();

        formData.append('location', JSON.stringify({
            name: locationName,
            weatherForecastUrl: forecastUrl
        }));

        return Vue.http.post(SETTINGS_API_ROOT + 'location', formData).then((response) => {
            return true;
        }, (response) => {
            console.log(response);
            return false;
        });
    }

}
