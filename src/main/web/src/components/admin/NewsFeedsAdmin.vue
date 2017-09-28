<template>
<div class="newsfeed-list">
    <h3>Flux d'actualités</h3>

    <b-link v-b-modal.newsFeedAddModal>
        <span><i class="fa fa-plus-square"></i>&nbsp;Ajouter un flux d'actualités</span>
    </b-link>

    <div v-if="newsFeedList">
        <table class="admin-newsfeed-table">
            <tbody>
                <tr v-for="newsFeed in newsFeedList" class="admin-newsfeed-row">
                    <td class="admin-newsfeed-playlist-col">
                        <span v-if="playlist && playlist.newsFeedsMap[newsFeed.id]"
                              class="admin-newsfeed-playlist-input admin-newsfeed-playlist-input"
                              @click="removeFromPlaylist(newsFeed)">
                            <i class="fa fa-star"></i>
                        </span>
                        <span v-if="playlist && !playlist.newsFeedsMap[newsFeed.id]"
                              class="admin-newsfeed-playlist-input admin-newsfeed-playlist-input"
                              @click="addToPlaylist(newsFeed)">
                            <i class="fa fa-star-o"></i>
                        </span>
                    </td>
                    <td class="admin-newsfeed-name-col">
                        <div class="admin-newsfeed-name">
                            <i class="fa fa-rss"></i>&nbsp&nbsp{{newsFeed.name}}
                        </div>
                    </td>
                    <td class="admin-newsfeed-delete-col">
                        <span class="admin-newsfeed-delete-input" @click="deleteNewsFeed(newsFeed)"><i class="fa fa-trash"></i></span>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <empty-content v-if="!newsFeed || newsFeedList.length == 0"
        message="Vous n'avez pas de flux d'actualités">
    </empty-content

    <!-- Modal pour ajouter un flux RSS -->
    <b-modal id="newsFeedAddModal"
             ref="newsFeedAddModal"
             title="Ajouter un flux d'actualités"
             size="lg">
        <news-feed-add-form></news-feed-add-form>
        <div slot="modal-footer">
            <b-btn variant="secondary" @click="closeNewsFeedAddModal">
                Fermer
            </b-btn>
        </div>
    </b-modal>
</div>
</template>

<script>
import { EventBus } from '@/services/EventBus.js';

import { cmsService } from '@/services/CmsService.js';
import { playlistService } from '@/services/PlaylistService.js';

import NewsFeedAddForm from '@/components/admin/NewsFeedAddForm.vue'
import EmptyContent from '@/components/admin/EmptyContent.vue';

export default {
    components: {
        'news-feed-add-form': NewsFeedAddForm,
        'empty-content': EmptyContent
    },

    data() {
        return {
            playlist: null,
            newsFeedList: null
        }
    },

    mounted() {
        this.getNewsFeedList();
        this.getPlaylist();
        EventBus.$on('newNewsFeedEvent', this.onNewNewsFeed);
    },

    beforeDestroy() {
        EventBus.$off('newNewsFeedEvent', this.onNewNewsFeed);
    },

    methods: {
        getNewsFeedList() {
            return cmsService.getNewsFeedList().then((newsFeedList) => {
                this.newsFeedList = newsFeedList;
            });
        },

        getPlaylist() {
            return playlistService.getPlaylist().then((playlist) => {
                this.playlist = playlist;
            });
        },

        removeFromPlaylist(newsFeed) {
            return playlistService.removeNewsFeedFromPlaylist(newsFeed).then((playlist) => {
                this.playlist.newsFeeds = playlist.newsFeeds;
                this.playlist.newsFeedsMap = playlist.newsFeedsMap;
            });
        },

        addToPlaylist(newsFeed) {
            return playlistService.addNewsFeedToPlaylist(newsFeed).then((playlist) => {
                this.playlist.newsFeeds = playlist.newsFeeds;
                this.playlist.newsFeedsMap = playlist.newsFeedsMap;
            });
        },

        deleteNewsFeed(newsFeed) {
            return cmsService.deleteNewsFeed(newsFeed.id).then(() => {
                return this.getNewsFeedList();
            })
        },

        closeNewsFeedAddModal() {
            this.$refs.newsFeedAddModal.hide();
        },

        onNewNewsFeed() {
            this.closeNewsFeedAddModal();
            this.getNewsFeedList();
        }
    }
}
</script>

<style scoped>
.newsfeed-list {
    margin-top: 10px;
    margin-bottom: 10px;
}

.admin-newsfeed-table {
    table-layout: auto;
    margin-top: 16px;
    border: 1px solid rgba(0, 0, 0, 0.2);
    width: 100%;
}

.admin-newsfeed-row {
    background-color: white;
    box-shadow: 1px 4px 6px rgba(0, 0, 0, .3);
    border: solid 1px rgba(0, 0, 0, 0.1);
}

.admin-newsfeed-row:hover {
    background-color: rgba(0, 123, 255, 0.6);
    color: white;
}

.admin-newsfeed-playlist-col {
    padding: 8px;
    text-align: center;
    font-size: 1.5em;
    width: 60px;
}

.admin-newsfeed-playlist-input {
    color: #E55934;
}

.admin-newsfeed-playlist-input:hover {
    cursor: pointer;
}

.admin-newsfeed-name-col {
    position: relative;
    padding-left: 8px;
}

.admin-newsfeed-name {
    font-size: 1.2em;
}

.admin-newsfeed-delete-col {
    padding: 4px;
    text-align: center;
    font-size: 1.5em;
    background-color: rgba(0,0,0,0.1);
    color: rgba(0,0,0,0.5);
    width: 40px;
}

.admin-newsfeed-delete-input:hover {
    color: rgba(255, 0, 0, 0.7);
    cursor: pointer;
}

</style>
