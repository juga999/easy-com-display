<template>
    <div>
        <b-navbar toggleable type="inverse" class="admin-navbar">
            <file-import-modal></file-import-modal>
            <news-feed-add-modal></news-feed-add-modal>

            <b-nav-toggle target="nav_collapse"></b-nav-toggle>

            <b-collapse is-nav id="nav_collapse">
                <b-nav is-nav-bar>
                    <b-nav-item>
                        <b-link @click="importPresentation">
                            <span><i class="fa fa-cloud-download"></i>&nbsp;Importer une présentation</span>
                        </b-link>
                    </b-nav-item>
                    <b-nav-item>
                        <b-link @click="addNewsFeed">
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
    </div>
</template>

<script>
import CmsService from '../../services/CmsService.js'

import FileImportModal from './FileImportModal.vue'
import NewsFeedAddModal from './NewsFeedAddModal.vue'

import StreamList from './StreamList.vue'
import NewsFeedList from './NewsFeedList.vue'
import PowerManagement from './PowerManagement.vue'
import ThisLocation from './Location.vue'

export default {
    components: {
        'file-import-modal': FileImportModal,
        'news-feed-add-modal': NewsFeedAddModal,
        'stream-list': StreamList,
        'newsfeed-list': NewsFeedList,
        'power-management': PowerManagement,
        'this-location': ThisLocation
    },

    data() {
        return {};
    },

    mounted() {
    },

    methods: {
        importPresentation() {
            this.$root.$emit('show::modal', 'fileImportModal');
        },

        addNewsFeed() {
            this.$root.$emit('show::modal', 'newsFeedAddModal');
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
