import { EventBus } from '@/services/EventBus.js';

import  {AbstractService} from '@/services/ServiceUtils.js';

const PLAYLIST_API_ROOT = '/api/ecd/'

class PlaylistService extends AbstractService {

    getPlaylist() {
        return this.get(PLAYLIST_API_ROOT + 'playlist').then((response) => {
            let result = response.body;
            return this._fillPlaylistMaps(result);
        }, (response) => {
            console.log(response);
            return null;
        });
    }

    addStreamToPlaylist(stream) {
        var model = {id: stream.id};
        return this.postJson(PLAYLIST_API_ROOT + 'playlist/add-stream', model).then((response) => {
            let result = response.body;
            return this._fillPlaylistMaps(result);
        }, (response) => {
            console.log(response);
            return null;
        });
    }

    removeStreamFromPlaylist(stream) {
        var model = {id: stream.id};
        return this.postJson(PLAYLIST_API_ROOT + 'playlist/remove-stream', model).then((response) => {
            let result = response.body;
            return this._fillPlaylistMaps(result);
        }, (response) => {
            console.log(response);
            return null;
        });
    }

    addNewsFeedToPlaylist(newsFeed) {
        var model = {id: newsFeed.id};
        return this.postJson(PLAYLIST_API_ROOT + 'playlist/add-newsfeed', model).then((response) => {
            let result = response.body;
            return this._fillPlaylistMaps(result);
        }, (response) => {
            console.log(response);
            return null;
        });
    }

    removeNewsFeedFromPlaylist(newsFeed) {
        var model = {id: newsFeed.id};
        return this.postJson(PLAYLIST_API_ROOT + 'playlist/remove-newsfeed', model).then((response) => {
            let result = response.body;
            return this._fillPlaylistMaps(result);
        }, (response) => {
            console.log(response);
            return null;
        });
    }
    
    _fillPlaylistMaps(playlist) {
        playlist.streamsMap = {};
        playlist.streams.forEach((stream) => {
            playlist.streamsMap[stream.id] = true;
        });
        playlist.newsFeedsMap = {};
        playlist.newsFeeds.forEach((newsFeed) => {
            playlist.newsFeedsMap[newsFeed.id] = true;
        })
        return playlist;
    }
}

export const playlistService = new PlaylistService();
