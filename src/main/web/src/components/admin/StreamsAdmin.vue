<template>
<div class="admin-stream-list">
    <h3>Fils d'annonces</h3>

    <b-link v-b-modal.streamAddModal>
        <span><i class="fa fa-plus-square"></i>&nbsp;Créer un fil d'annonces</span>
    </b-link>
    &nbsp;&nbsp;
    <b-link v-b-modal.presentationImportModal>
        <span><i class="fa fa-cloud-download"></i>&nbsp;Importer une présentation</span>
    </b-link>

    <div v-if="streamList">
        <div v-for="stream in streamList" class="admin-stream-item-wrapper">
            <div class="admin-stream-item-thumbnail">
                <router-link :to="{ name: 'stream-detail', params: { id: stream.id }}">
                    <img v-if="stream.frames.length > 0" :src="cmsService.getFrameThumbnailSource(stream, 0)">
                    <span v-if="stream.frames.length == 0" class="empty-stream-thumbnail"><i class="fa fa-folder fa-5x"></i></span>
                </router-link>
            </div>
            <div class="admin-stream-item-info">
                <div class="admin-stream-item-name">
                    {{stream.name}}
                    <div class="admin-stream-item-controls">
                        <span class="admin-stream-item-trash" @click="showStreamDeletionConfirmModal(stream)"><i class="fa fa-trash"></i></span>
                    </div>
                </div>
                <div class="admin-stream-item-date">
                    Mis à jour le {{getLastUpdateDate(stream)}}
                </div>
            </div>
        </div>
    </div>

    <!-- Modal pour ajouter un flux d'annonces -->
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

    <!-- Modal pour supprimer un flux d'annonces -->
    <b-modal id="streamDeletionConfirmModal"
             ref="streamDeletionConfirmModal"
             title="Suppression d'un fil d'actualités">
        <span v-if="streamToDelete" >Voulez-vous supprimer "{{streamToDelete.name}}" ?</span>

        <div slot="modal-footer">
            <b-button variant="danger"
                      @click="confirmStreamDeletion"
                      href="">
                <i class="fa fa-trash"></i> Oui, supprimer
            </b-button>
            <b-button variant="secondary"
                      @click="closeStreamDeletionConfirmModal"
                      href="">
                Non, annuler
            </b-button>
        </div>
    </b-modal>
</div>
</template>

<script>
import { EventBus } from '@/services/EventBus.js';

import { cmsService } from '@/services/CmsService.js';

import StreamAddForm from '@/components/admin/StreamAddForm.vue';
import PresentationImportForm from '@/components/admin/PresentationImportForm.vue';

export default {
    components: {
        'stream-add-form': StreamAddForm,
        'presentation-import-form': PresentationImportForm
    },

    data() {
        return {
            cmsService,
            streamList: null,
            currentStreamId: null,
            streamToDelete: null
        };
    },

    mounted() {
        this.getStreamList();
        EventBus.$on('newStreamEvent', this.onNewStream);
    },

    beforeDestroy() {
        EventBus.$off('newStreamEvent', this.onNewStream);
    },

    methods: {
        getStreamList() {
            return cmsService.getStreamList().then((streamList) => {
                this.streamList = streamList;
            });
        },

        showStreamDeletionConfirmModal(stream) {
            this.streamToDelete = stream;
            this.$refs.streamDeletionConfirmModal.show();
        },

        confirmStreamDeletion() {
            this.deleteStream().then(() => this.closeStreamDeletionConfirmModal());
        },

        deleteStream() {
            return cmsService.deleteStream(this.streamToDelete.id).then(() => {
                return this.getStreamList()
            });
        },

        getLastUpdateDate(stream) {
            let lastUpdateDate = new Date(stream.lastUpdateDateTime);
            let day = lastUpdateDate.getDate();
            let month = lastUpdateDate.getMonth() + 1;
            let year = lastUpdateDate.getFullYear();
            let hours = lastUpdateDate.getHours();
            let minutes = lastUpdateDate.getMinutes();
            return day + '/' + month + '/' + year + ' à ' + hours + ':' + minutes;
        },

        onNewStream() {
            this.closeStreamAddModal();
            this.closePresentationImportModal();
            this.getStreamList();
        },

        closeStreamAddModal() {
            this.$refs.streamAddModal.hide();
        },

        closePresentationImportModal() {
            this.$refs.presentationImportModal.hide();
        },

        closeStreamDeletionConfirmModal() {
            this.$refs.streamDeletionConfirmModal.hide();
            this.streamToDelete = null;
        }
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
    vertical-align: middle;
    margin: 20px;
    padding: 8px;
    box-shadow: 1px 4px 6px rgba(0, 0, 0, .3);
    border: solid 1px rgba(0, 0, 0, 0.1);
}

.admin-stream-item-wrapper:hover {
    border: solid 1px rgba(0, 123, 255, 0.6);
}

.admin-stream-item-thumbnail {
    display: inline-block;
    padding: 4px;
}

.admin-stream-item-info {
    display: inline-block;
    vertical-align: top;
    font-size:1.1em;
}

.admin-stream-item-name {
    font-size: 1.2em;
    border-bottom: 1px solid rgba(0, 0, 0, 0.2);
    margin-bottom: 8px;
    padding: 2px;
}

.admin-stream-item-date {
    font-size: 0.9em;
    font-style:italic;
    color: rgba(0, 0, 0, 0.8);
}

.admin-stream-item-controls {
    float: right;
}

.admin-stream-item-trash {
    color: rgba(255, 0, 0, 0.7);
}

.admin-stream-item-trash:hover {
    cursor: pointer;
}

.empty-stream-thumbnail {
    color: #A9A9A9;
    vertical-align: middle;
}
</style>
