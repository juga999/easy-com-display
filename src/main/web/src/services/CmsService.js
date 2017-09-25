import { EventBus } from '@/services/EventBus.js';

import  {AbstractService} from '@/services/ServiceUtils.js';

const CMS_API_ROOT = '/api/ecd/cms/';
const MEDIA_ROOT = '/media/ecd/';

class CmsService extends AbstractService {

    getCurrentStreamId() {
        let url = CMS_API_ROOT + 'current-stream-id';
        return this.get(url).then((response) => {
            let result = response.body;
            return result.streamId || null;
        }, (response) => {
            console.log(response);
            return null;
        });
    }

    getStreamList() {
        let url = CMS_API_ROOT + 'streams';
        return this.get(url).then((response) => {
            return response.body;
        }, (response) => {
            console.log(response);
            return null;
        });
    }

    getStreamDetail(streamId) {
        let url = CMS_API_ROOT + 'streams/' + streamId;
        return this.get(url).then((response) => {
            return response.body;
        }, (response) => {
            console.log(response.body);
            return {
                status: 'error'
            }
        });
    }

    getFrameThumbnailSource(stream, index) {
        return MEDIA_ROOT + 'streams/' + stream.id + '/' + stream.frames[index].thumbnail;
    }

    getFrame(stream, index) {
        if (stream && index < stream.frames.length) {
            let frame = stream.frames[index];
            frame.url = 'url(' + MEDIA_ROOT + 'streams/' + stream.id + '/' + frame.src + ')';
            return frame;
        } else {
            return null;
        }
    }

    saveStreamTiming(streamId, timing) {
        let formData = new FormData();

        formData.append('streamId', streamId);
        formData.append('timing', timing);

        const url = CMS_API_ROOT + 'streams/timing'
        return this.postFormData(url, formData);
    }

    deleteStream(streamId) {
        let url = CMS_API_ROOT + 'streams/' + streamId
        return this.delete(url).then((response) => {
            return response.body;
        }, (response) => {
            console.log(response.body);
            return {
                status: 'error'
            }
        });
    }

    addStream(streamName) {
        let formData = new FormData();

        formData.append('streamName', streamName);

        let url = CMS_API_ROOT + 'streams';
        return this.postFormData(url, formData).then((response) => {
            EventBus.$emit('newStreamEvent', response.body.streamId);
            return response.body;
        }, (response) => {
            console.log(response.body);
            return response.body;
        });
    }

    importPresentation(presentationFile, streamName) {
        let formData = new FormData();

        formData.append('presentationFile', presentationFile);
        formData.append('streamName', streamName);

        let url = CMS_API_ROOT + 'streams';
        return this.postFormData(url, formData).then((response) => {
            EventBus.$emit('newStreamEvent', response.body.streamId);
            return response.body;
        }, (response) => {
            console.log(response.body);
            return response.body;
        });
    }

    addStreamFrame(streamId, newFrameIndex, imageFile) {
        let formData = new FormData();
        
        formData.append('streamId', streamId);
        formData.append('newFrameIndex', newFrameIndex);
        formData.append('imageFile', imageFile);

        let url = CMS_API_ROOT + 'streams/' + streamId + '/frame';
        return this.postFormData(url, formData).then((response) => {
            EventBus.$emit('updatedStreamEvent', streamId);
            return response.body;
        }, (response) => {
            console.log(response.body);
            return response.body;
        });
    }

    getNewsFeedList() {
        let url = CMS_API_ROOT + 'newsfeeds';
        return this.get(url).then((response) => {
            let result = response.body;
            return result;
        }, (response) => {
            console.log(response);
            return null;
        });
    }

    addNewsFeed(newsFeed) {
        let url = CMS_API_ROOT + 'newsfeeds';
        return this.postJson(url, newsFeed).then((response) => {
            EventBus.$emit('newNewsFeedEvent', response.body.newsFeedId);
            return response.body;
        }, (response) => {
            console.log(response.body);
            return response.body;
        });
    }

    deleteNewsFeed(newsFeedId) {
        let url = CMS_API_ROOT + 'newsfeeds/' + newsFeedId;
        return this.delete(url).then((response) => {
            return response.body;
        }, (response) => {
            console.log(response.body);
            return {
                status: 'error'
            }
        });
    }

    getAggregatedNewsFeed() {
        let url = CMS_API_ROOT + 'news';
        return this.get(url).then((response) => {
            let result = response.body;
            return result;
        }, (response) => {
            console.log(response);
            return null;
        });
    }

    getWeatherForecast() {
        let url = CMS_API_ROOT + 'weather';
        return this.get(url).then((response) => {
            let result = response.body;
            return result;
        }, (response) => {
            console.log(response);
            return null;
        })
    }

}

export const cmsService = new CmsService();
