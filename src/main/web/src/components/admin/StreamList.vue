<template>
    <div class="admin-stream-list">
        <stream-deletion-confirm-modal  v-on:confirmStreamDeletion="deleteStream" :stream="streamToDelete"></stream-deletion-confirm-modal>

        <h3>Présentations disponibles</h3>
        <div v-if="streamList">
            <div v-for="stream in streamList" class="admin-stream-item-wrapper">
                <b-card :header="stream.name">
                    <div v-if="!stream.frames" class="admin-stream-item-thumbnail">
                        <i class="fa fa-spinner fa-pulse fa-4x"></i>
                    </div>
                    <div v-if="stream.frames" class="admin-stream-item-thumbnail">
                        <router-link :to="{ name: 'stream-detail', params: { id: stream.id }}">
                            <img :src="CmsService.getFrameThumbnailSource(stream, 0)">
                        </router-link>
                    </div>
                    <div v-if="stream.frames">
                        <b-button variant="secondary" class="admin-stream-fav" @click="toggleStream(stream)">
                            <i v-if="stream.id != currentStreamId" class="fa fa-star-o"></i>
                            <i v-if="stream.id == currentStreamId" class="fa fa-star"></i>
                        </b-button>
                        <b-button variant="outline-danger" class="admin-stream-trash" @click="showStreamDeletionConfirmModal(stream)">
                            <i class="fa fa-trash"></i>
                        </b-button>
                    </div>
                </b-card>
            </div>
        </div>
    </div>
</template>

<script>
import CmsService from '../../services/CmsService.js'

import StreamDeletionConfirmModal from './StreamDeletionConfirmModal'

export default {
    components: {
        'stream-deletion-confirm-modal': StreamDeletionConfirmModal
    },

    data() {
        return {
            CmsService,
            streamList: null,
            currentStreamId: null,
            streamToDelete: null
        };
    },

    mounted() {
        this.getStreamList();
        this.getCurrentStreamId();

        /*this.refreshId = window.setInterval(() => {
            this.getStreamList();
            this.getCurrentStreamId();
        }, 1500);*/
    },

    beforeDestroy() {
        if (this.refreshId) {
            window.clearInterval(this.refreshId);
        }
    },

    methods: {
        getStreamList() {
            return CmsService.getStreamList().then((streamList) => {
                this.streamList = streamList;
            });
        },

        getCurrentStreamId() {
            return CmsService.getCurrentStreamId().then((streamId) => {
                this.currentStreamId = streamId;
            });
        },

        toggleStream(stream) {
            return CmsService.toggleStream(stream.id).then(() => {
                this.getCurrentStreamId()
            });
        },

        showStreamDeletionConfirmModal(stream) {
            this.streamToDelete = stream;
            this.$root.$emit('show::modal', 'streamDeletionConfirmModal');
        },

        deleteStream(streamId) {
            return CmsService.deleteStream(streamId).then(() => {
                return this.getStreamList()
            });
        },
    }
}
</script>

<style scoped>
.admin-stream-list {
    margin-top: 10px;
    margin-bottom: 10px;
}

.admin-stream-item-wrapper {
    display: inline-block;
    margin: 20px;
    min-width: 180px;
    min-height: 120px;
}

.admin-stream-item-thumbnail {
    text-align: center;
    padding-bottom: 4px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.2);
    margin-bottom: 10px;
}

.admin-stream-fav {
    float: left;
}

.admin-stream-trash {
    float: right;
}
</style>
