<template>
    <b-form @submit.prevent="addNewsFeed">
        <b-form-group id="newsFeedNameGroup"
                      label="Nom" label-for="newsFeedNameInput">
            <b-form-input id="newsFeedNameInput"
                          v-model="newsFeedName"
                          type="text"
                          placeholder="Entrer le nom du flux d'actualités">
            </b-form-input>
        </b-form-group>
        <b-form-group id="newsFeedUrlGroup"
                      label="Adresse" label-for="newsFeedUrlInput">
            <b-form-input id="newsFeedUrlInput"
                          v-model="newsFeedUrl"
                          type="text"
                          placeholder="Entrer l'URL du flux d'actualités">
            </b-form-input>
        </b-form-group>

        <div class="centered-btn-wrapper">
            <b-button type="submit" class="import-btn"
                        :disabled="!newsFeedName || !newsFeedUrl"
                        variant="primary">
                Ajouter
            </b-button>
        </div> 
    </b-form>
</template>

<script>
import CmsService from '@/services/CmsService.js'

export default {
    data() {
        return {
            newsFeedName: '',
            newsFeedUrl: ''
        };
    },

    methods: {
        addNewsFeed() {
            if (!this.newsFeedUrl) {
                return;
            }

            CmsService.addNewsFeed(this.newsFeedName, this.newsFeedUrl);
        }
    }
}
</script>

<style scoped>
.centered-btn-wrapper {
   text-align: center;
}
</style>