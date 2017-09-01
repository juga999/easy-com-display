<template>
    <div>
        <b-navbar toggleable type="inverse" class="admin-navbar">
            <b-nav-toggle target="nav_collapse"></b-nav-toggle>
            <b-collapse is-nav id="nav_collapse">
                <b-nav is-nav-bar>
                    <b-nav-item>
                        <b-link v-b-modal.streamAddModal>
                            <span><i class="fa fa-plus-square"></i>&nbsp;Créer une présentation</span>
                        </b-link>
                    </b-nav-item>
                    <b-nav-item>
                        <b-link v-b-modal.presentationImportModal>
                            <span><i class="fa fa-cloud-download"></i>&nbsp;Importer une présentation</span>
                        </b-link>
                    </b-nav-item>
                    <b-nav-item>
                        <b-link v-b-modal.newsFeedAddModal>
                            <span><i class="fa fa-rss-square"></i>&nbsp;Ajouter un flux d'actualités</span>
                        </b-link>
                    </b-nav-item>
                </b-nav>
            </b-collapse>
        </b-navbar>

        <div class="row">
            <div class="col-md-6">
                <stream-list></stream-list>
            </div>
            <div class="col-md-6">
                <newsfeed-list></newsfeed-list>
            </div>
        </div>

        <div class="row">
            <div class="col-md-4">
                <power-management></power-management>
            </div>
            <div class="col-md-4">
                <this-location></this-location>
            </div>
        </div>

        <!-- Modal pour ajouter un flux d'information -->
        <b-modal id="streamAddModal"
                 ref="streamAddModal"
                 title="Créer un fil d'annonces" 
                 size="lg">
            <stream-add-form></stream-add-form>
            <div slot="modal-footer">
                <b-btn variant="secondary" @click="closeStreamAddModal">
                    Fermer
                </b-btn>
            </div>
        </b-modal>

        <!-- Modal pour importer un présentation -->
        <b-modal id="presentationImportModal"
                 ref="presentationImportModal"
                 title="Importer une présentation" 
                 size="lg">
            <presentation-import-form></presentation-import-form>
            <div slot="modal-footer">
                <b-btn variant="secondary" @click="closePresentationImportModal">
                    Fermer
                </b-btn>
            </div>
        </b-modal>

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

import CmsService from '@/services/CmsService.js'

import StreamAddForm from '@/components/admin/StreamAddForm.vue'
import PresentationImportForm from '@/components/admin/PresentationImportForm.vue'
import NewsFeedAddForm from '@/components/admin/NewsFeedAddForm.vue'

import StreamList from '@/components/admin/StreamList.vue'
import NewsFeedList from '@/components/admin/NewsFeedList.vue'
import PowerManagement from '@/components/admin/PowerManagement.vue'
import ThisLocation from '@/components/admin/Location.vue'

export default {
    components: {
        'stream-add-form': StreamAddForm,
        'presentation-import-form': PresentationImportForm,
        'news-feed-add-form': NewsFeedAddForm,
        'stream-list': StreamList,
        'newsfeed-list': NewsFeedList,
        'power-management': PowerManagement,
        'this-location': ThisLocation
    },

    data() {
        return {};
    },

    mounted() {
        EventBus.$on('newStreamEvent', this.onNewStream);
        EventBus.$on('newNewsFeedEvent', this.onNewNewsFeed);
    },

    beforeDestroy() {
        EventBus.$off('newStreamEvent', this.onNewStream);
        EventBus.$off('newNewsFeedEvent', this.onNewNewsFeed);
    },

    methods: {
        onNewStream(streamId) {
            this.closeStreamAddModal();
            this.closePresentationImportModal();
        },

        onNewNewsFeed(newsFeedId) {
            this.closeNewsFeedAddModal();
        },

        closeStreamAddModal() {
            this.$refs.streamAddModal.hide();
        },

        closePresentationImportModal() {
            this.$refs.presentationImportModal.hide();
        },

        closeNewsFeedAddModal() {
            this.$refs.newsFeedAddModal.hide();
        }
    }
}

</script>

<style scoped>
.admin-navbar {
    background-color: #E55934;
}

.admin-navbar-brand {
  color: white;
}

.admin-navbar a {
    cursor: pointer;
    text-decoration: none;
    color: white;
}
</style>
