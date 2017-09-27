import Vue from 'vue'
import VueResource from 'vue-resource'

Vue.use(VueResource);

export class AbstractService {

    get(url) {
        return Vue.http.get(url);
    }

    postJson(url, obj) {
        let headers = {"Content-Type": "application/json"};
        return Vue.http.post(url, JSON.stringify(obj), {headers});
    }

    postFormData(url, formData) {
        return Vue.http.post(url, formData);
    }

    delete(url) {
        return Vue.http.delete(url);
    }

}
