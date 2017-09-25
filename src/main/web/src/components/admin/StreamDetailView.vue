<template>
    <div class="row">
        <div class="col-sm-2 navigation-pane">
            <b-list-group>
                <b-list-group-item>
                    <router-link to="/admin">
                        <span class="navigation-menu-entry-icon"><i class="fa fa-reply"></i></span>
                        Retour
                    </router-link>
                </b-list-group-item>
                <b-list-group-item>
                    <router-link :to="{ name: 'stream-view', params: { id: streamId }}">
                        <span class="navigation-menu-entry-icon"><i class="fa fa-desktop"></i></span>
                        Aperçu
                    </router-link>
                </b-list-group-item>
            </b-list-group>
        </div>

        <div v-if="stream" class="col-md-9 stream-detail-pane">
            <h3>{{stream.name}}</h3>
            <div class="row">
                <div class="col-md-9 stream-thumbnail-list-wrapper">
                    <div class="stream-thumbnail-list">
                        <div v-if="stream.frames.length == 0" class="stream-thumbnail-wrapper">
                            <div class="stream-add-btn">
                                <b-link @click="insertFrame(0)" ><i class="fa fa-plus-circle fa-3x"></i></b-link>
                            </div>
                        </div>
                        <div class="stream-thumbnail-wrapper" v-for="(frame, index) in stream.frames">
                            <div v-if="index == 0" class="stream-add-btn">
                                <b-link @click="insertFrame(0)"><i class="fa fa-plus-circle fa-3x"></i></b-link>
                            </div>
                            <img class="stream-thumbnail" :src="cmsService.getFrameThumbnailSource(stream, index)">
                            <div class="stream-add-btn">
                                <b-link @click="insertFrame(index + 1)"><i class="fa fa-plus-circle fa-3x"></i></b-link>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <b-card header="Minuteur">

                        <form @submit.prevent="saveStreamTiming">
                            <div class="form-group">
                                <label for="streamTiming">Secondes entre chaque diapo</label>
                                <input type="number" v-model="streamTiming" class="form-control" id="streamTiming">
                            </div>
                            <div class="centered-btn-wrapper">
                                <b-button type="submit" class="save-btn"
                                        variant="primary">
                                    Enregistrer
                                </b-button>
                            </div>
                        </form>
                    </b-card>
                </div>
            </div>
        </div>

        <!-- Modal pour insérer une image au fil d'annonces -->
        <b-modal id="streamFrameAddModal"
                 ref="streamFrameAddModal"
                 title="Insérer une image">
            <stream-frame-add-form :streamId="streamId" :newFrameIndex="newFrameIndex"></stream-frame-add-form>
            <div slot="modal-footer">
                <b-btn variant="secondary" @click="closeStreamFrameAddModal">
                    Fermer
                </b-btn>
            </div>
        </b-modal>

    </div>
</template>

<script>
import { EventBus } from '@/services/EventBus.js';

import { cmsService } from '@/services/CmsService.js';

import StreamFrameAddForm from '@/components/admin/StreamFrameAddForm.vue';

export default {
    components: {
        'stream-frame-add-form': StreamFrameAddForm
    },

    data() {
        return {
            cmsService,
            streamId: this.$route.params.id,
            stream: null,
            streamTiming: null,
            newFrameIndex: null
        }
    },

    mounted() {
        this.getStreamDetail();
        EventBus.$on('updatedStreamEvent', this.onStreamUpdate);
    },

    beforeDestroy() {
        EventBus.$off('updatedStreamEvent', this.onStreamUpdate);
    },

    methods: {
        getStreamDetail() {
            cmsService.getStreamDetail(this.streamId).then((stream) => {
                this.stream = stream;
                this.streamTiming = stream.timing;
            });
        },

        saveStreamTiming() {
            cmsService.saveStreamTiming(this.streamId, this.streamTiming);
        },

        insertFrame(index) {
            this.newFrameIndex = index;
            this.$refs.streamFrameAddModal.show();
        },

        onStreamUpdate(streamId) {
            if (this.streamId === streamId) {
                this.closeStreamFrameAddModal();
                this.getStreamDetail();
            }
        },

        closeStreamFrameAddModal() {
            this.newFrameIndex = null;
            this.$refs.streamFrameAddModal.hide();
        }
    }
}
</script>

<style scoped>
h3 {
    color: #E55934;
    padding: 4px;
    border-bottom: solid 1px;
    font-size: medium;
}

.stream-detail-pane {
    padding-top: 20px;
    padding-left: 20px;
}

.stream-name {
    color: black;
    border-bottom: 1px solid;
}

.stream-thumbnail-list-wrapper {
    display: table;
}

.stream-thumbnail-list {
    display: table-cell;
}

.stream-thumbnail-wrapper {
    display: inline-block;
    min-width: 80px;
    min-height: 80px;
}

.stream-thumbnail {
    margin: 10px;
    padding: 4px;
    box-shadow: 1px 1px 6px #555;
}

.stream-add-btn {
    display: inline-block;
    vertical-align: middle;
    margin: 16px;
}

.centered-btn-wrapper {
   text-align: center;
}

</style>
