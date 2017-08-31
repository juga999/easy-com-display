<template>
    <b-modal id="newsFeedAddModal" 
             title="Ajouter un flux d'actualités" 
             size="lg">
        <form @submit.prevent="addNewsFeed">
            <div class="form-group">
                <label for="newsFeedName">Nom</label>
                <input v-model="newsFeedName" type="text" class="form-control" id="newsFeedName" placeholder="Entrer le nom du flux d'actualités">
            </div>
            <div class="form-group">
                <label for="newsFeedUrl">Adresse</label>
                <input v-model="newsFeedUrl" type="text" class="form-control" id="newsFeedUrl" placeholder="Entrer l'URL du flux d'actualités">
            </div>
            <div class="centered-btn-wrapper">
                <b-button type="submit" class="import-btn"
                         :disabled="!newsFeedName || !newsFeedUrl"
                         variant="primary">
                    Ajouter
                </b-button>
            </div> 
        </form>

        <div slot="modal-footer">
            <b-button variant="secondary"
                      @click="closeNewsFeedAddModal"
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
            newsFeedName: '',
            newsFeedUrl: ''
        };
    },

    methods: {
        closeNewsFeedAddModal() {
            this.$root.$emit('hide::modal', 'newsFeedAddModal');
        },

        addNewsFeed() {
            if (!this.newsFeedUrl) {
                return;
            }

            CmsService.addNewsFeed(this.newsFeedName, this.newsFeedUrl);

            this.closeNewsFeedAddModal();
        }
    }
}

</script>

<style scoped>

.centered-btn-wrapper {
   text-align: center;
}

</style>
