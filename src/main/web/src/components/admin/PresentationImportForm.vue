<template>
    <b-form @submit.prevent="importPresentation">
        <b-form-group id="streamNameGroup"
                      label="Nom" label-for="streamNameInput">
            <b-form-input id="streamNameInput"
                          v-model="streamName"
                          type="text"
                          placeholder="Entrer le nom du fil d'annonces">
            </b-form-input>
        </b-form-group>
        <b-form-group id="presentationFileGroup"
                      label="Fichier" label-for="presentationFileInput">
            <b-form-file id="presentationFileInput"
                         v-model="presentationFile"
                         plain
                         choose-label="Choisissez une présentation"
                         placeholder="Aucun fichier choisi">
            </b-form-file>
        </b-form-group>

        <div class="centered-btn-wrapper">
            <b-button type="submit" class="import-btn"
                      :disabled="busy || !presentationFile || !streamName"
                      variant="primary">
                Importer <i v-if="busy" class="fa fa-spinner fa-spin"></i>
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
            presentationFile: null,
            streamName: null
        };
    },

    methods: {
        importPresentation() {
            if (!this.presentationFile) {
                return;
            }

            this.busy = true;
            CmsService.importPresentation(this.presentationFile, this.streamName)
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
