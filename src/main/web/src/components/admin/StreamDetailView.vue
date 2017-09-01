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
                <div class="col-md-8">
                    <div class="stream-thumbnail-list">
                        <div class="stream-thumbnail">
                            TODO
                        </div>
                        <div class="stream-thumbnail" v-for="(frame, index) in stream.frames">
                            <img :src="CmsService.getFrameThumbnailSource(stream, index)">
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
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
    </div>
</template>

<script>
import CmsService from '@/services/CmsService.js'

export default {
    data() {
        return {
            CmsService,
            streamId: this.$route.params.id,
            stream: null,
            streamTiming: null
        }
    },

    mounted() {
        CmsService.getStreamDetail(this.streamId).then((stream) => {
            this.stream = stream;
            this.streamTiming = stream.timing;
        })
    },

    methods: {
        saveStreamTiming() {
            CmsService.saveStreamTiming(this.streamId, this.streamTiming);
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

.stream-thumbnail {
    float: left;
    margin: 10px;
    padding: 6px;
    border-radius: 6px;
    min-width: 80px;
    min-height: 80px;
    box-shadow: 1px 1px 6px #555;
}

.centered-btn-wrapper {
   text-align: center;
}

</style>
