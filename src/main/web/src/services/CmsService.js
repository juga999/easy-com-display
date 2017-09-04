import Vue from 'vue'
import VueResource from 'vue-resource'

import { EventBus } from '@/services/EventBus.js';

Vue.use(VueResource);

const CMS_API_ROOT = '/api/ecd/cms/'
const MEDIA_ROOT = '/media/ecd/'

export default {

    getCurrentStreamId() {
        return Vue.http.get(CMS_API_ROOT + 'current-stream-id').then((response) => {
            let result = response.body;
            return result.streamId || null;
        }, (response) => {
            console.log(response);
            return null;
        });
    },

    getStreamList() {
        return Vue.http.get(CMS_API_ROOT + 'streams').then((response) => {
            return response.body;
        }, (response) => {
            console.log(response);
            return null;
        });
    },

    getStreamDetail(streamId) {
        return Vue.http.get(CMS_API_ROOT + 'streams/' + streamId).then((response) => {
            return response.body;
        }, (response) => {
            console.log(response.body);
            return {
                status: 'error'
            }
        });
    },

    getFrameThumbnailSource(stream, index) {
        return MEDIA_ROOT + 'streams/' + stream.id + '/' + stream.frames[index].thumbnail;
    },

    getFrame(stream, index) {
        if (stream && index < stream.frames.length) {
            let frame = stream.frames[index];
            frame.url = 'url(' + MEDIA_ROOT + 'streams/' + stream.id + '/' + frame.src + ')';
            return frame;
        } else {
            return null;
        }
    },

    toggleStream(streamId) {
        let formData = new FormData();

        formData.append('streamId', streamId);

        return Vue.http.post(CMS_API_ROOT + 'streams/toggle', formData);
    },

    saveStreamTiming(streamId, timing) {
        let formData = new FormData();

        formData.append('streamId', streamId);
        formData.append('timing', timing);

        return Vue.http.post(CMS_API_ROOT + 'streams/timing', formData);
    },

    deleteStream(streamId) {
        return Vue.http.delete(CMS_API_ROOT + 'streams/' + streamId).then((response) => {
            return response.body;
        }, (response) => {
            console.log(response.body);
            return {
                status: 'error'
            }
        });
    },

    addStream(streamName) {
        let formData = new FormData();

        formData.append('streamName', streamName);

        return new Promise(function(resolve, reject) {
            Vue.http.post(CMS_API_ROOT + 'streams', formData).then((response) => {
                EventBus.$emit('newStreamEvent', response.body.streamId);
                resolve(response.body);
            }, (response) => {
                console.log(response.body);
                reject(response.body);
            });
        });
    },

    importPresentation(presentationFile, streamName) {
        let formData = new FormData();

        formData.append('presentationFile', presentationFile);
        formData.append('streamName', streamName);

        return new Promise(function(resolve, reject) {
            Vue.http.post(CMS_API_ROOT + 'streams', formData).then((response) => {
                EventBus.$emit('newStreamEvent', response.body.streamId);
                resolve(response.body);
            }, (response) => {
                console.log(response.body);
                reject(response.body);
            });
        });
    },

    addStreamFrame(streamId, newFrameIndex, imageFile) {
        let formData = new FormData();
        
        formData.append('streamId', streamId);
        formData.append('newFrameIndex', newFrameIndex);
        formData.append('imageFile', imageFile);

        return new Promise(function(resolve, reject) {
            Vue.http.post(CMS_API_ROOT + 'streams/' + streamId + '/frame', formData).then((response) => {
                EventBus.$emit('updatedStreamEvent', streamId);
                resolve(response.body);
            }, (response) => {
                console.log(response.body);
                reject(response.body);
            });
        });
    },

    getNewsFeedList() {
        return Vue.http.get(CMS_API_ROOT + 'newsfeeds').then((response) => {
            let result = response.body;
            return result;
        }, (response) => {
            console.log(response);
            return null;
        });
    },

    addNewsFeed(newsFeedName, newsFeedUrl) {
        let formData = new FormData();

        formData.append('newsFeedName', newsFeedName);
        formData.append('newsFeedUrl', newsFeedUrl);

        return new Promise(function(resolve, reject) {
            Vue.http.post(CMS_API_ROOT + 'newsfeeds', formData).then((response) => {
                EventBus.$emit('newNewsFeedEvent', response.body.newsFeedId);
                resolve(response.body);
            }, (response) => {
                console.log(response.body);
                reject(response.body);
            });
        });
    },

    deleteNewsFeed(newsFeedId) {
        return Vue.http.delete(CMS_API_ROOT + 'newsfeeds/' + newsFeedId).then((response) => {
            return response.body;
        }, (response) => {
            console.log(response.body);
            return {
                status: 'error'
            }
        });
    },

    getAggregatedNewsFeed() {
        return Vue.http.get(CMS_API_ROOT + 'news').then((response) => {
            let result = response.body;
            return result;
        }, (response) => {
            console.log(response);
            return null;
        });
    },

    getWeatherForecast() {
        return Vue.http.get(CMS_API_ROOT + 'weather').then((response) => {
            let result = response.body;
            return result;
        }, (response) => {
            console.log(response);
            return null;
        })
    }

}
