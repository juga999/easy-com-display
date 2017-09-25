<template>
<div class="newsfeed-list">
    <h3>Flux d'actualités</h3>

    <b-link v-b-modal.newsFeedAddModal>
        <span><i class="fa fa-plus-square"></i>&nbsp;Ajouter un flux d'actualités</span>
    </b-link>

    <div v-if="newsFeedList">
        <table class="newsfeed-list-table">
            <tr v-for="newsFeed in newsFeedList">
                <td class="newsfeed-name">
                    {{newsFeed.name}}
                </td>
                <td>
                    <b-button variant="outline-danger" @click="deleteNewsFeed(newsFeed.id)">
                        <i class="fa fa-trash"></i>
                    </b-button>
                </td>
            </tr>
        </table>
    </div>

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

import NewsFeedAddForm from '@/components/admin/NewsFeedAddForm.vue'

export default {
    components: {
        'news-feed-add-form': NewsFeedAddForm
    },

    data() {
        return {
            newsFeedList: null
        }
    },

    mounted() {
        this.getNewsFeedList();
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

        deleteNewsFeed(newsFeedId) {
            return cmsService.deleteNewsFeed(newsFeedId).then(() => {
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

.newsfeed-list-table {
    width: 90%;
}

.newsfeed-list-table tr {
    border: 1px solid rgba(0, 0, 0, 0.2);;
}

.newsfeed-list-table tr:nth-child(odd) {
    background-color: #f7f7f9;
}

.newsfeed-list-table td {
    padding: 4px;
}

td.newsfeed-name {
    width: 80%;
}
</style>
