<template>
    <b-form @submit.prevent="addStream">
        <b-form-group id="streamNameGroup"
                      label="Nom" label-for="streamNameInput">
            <b-form-input id="streamNameInput"
                          v-model="streamName"
                          type="text"
                          placeholder="Entrer le nom du fil d'annonces">
            </b-form-input>
        </b-form-group>

        <div class="centered-btn-wrapper">
            <b-button type="submit" class="import-btn"
                      :disabled="busy || !streamName"
                      variant="primary">
                Ajouter <i v-if="busy" class="fa fa-spinner fa-spin"></i>
            </b-button>
        </div>   
    </b-form>
</template>

<script>
import CmsService from '@/services/CmsService.js'

export default {
    data() {
        return {
            busy: false,
            streamName: null
        };
    },

    methods: {
        addStream() {
            this.busy = true;
            CmsService.addStream(this.streamName)
                .then((result) => {
                    this.busy = false;
                });
        }
    }
}
</script>

<style scoped>
.centered-btn-wrapper {
   text-align: center;
}
</style>
