<template>
    <b-modal id="fileImportModal" 
             title="Importer une présentation" 
             size="lg">

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
                             choose-label="Choisissez une présentation"
                             placeholder="Aucun fichier choisi">
                </b-form-file>
            </b-form-group>

            <div class="centered-btn-wrapper">
                <b-button type="submit" class="import-btn"
                         :disabled="!presentationFile || !streamName"
                         variant="primary">
                    Importer
                </b-button>
            </div>   
        </b-form>

        <!--<b-alert :show="importError != null" variant="danger">
            <div v-if="importError && !importError.message">L'import a échoué</div>
            <div v-if="importError && importError.message">{{ importError.message }}</div>
        </b-alert>-->

        <div slot="modal-footer">
            <b-button variant="secondary"
                      @click="closeFileImportModal"
                      href="">
                Fermer
            </b-button>
        </div>
    </b-modal>
</template>

<script>
import CmsService from '@/services/CmsService.js'

export default {
    data() {
        return {
            presentationFile: null,
            streamName: null
        };
    },

    methods: {
        closeFileImportModal() {
            this.$root.$emit('hide::modal', 'fileImportModal');
        },

        importPresentation() {
            if (!this.presentationFile) {
                return;
            }

            this.importError = null;

            CmsService.importPresentation(this.presentationFile, this.streamName);

            //this.$router.push({name: 'stream-detail', params: {id: stream.stream_id}});
            this.closeFileImportModal();
        }
    }
}

</script>

<style scoped>

.presentation-input {
    width: 100%;
}

.centered-btn-wrapper {
   text-align: center;
}

</style>

