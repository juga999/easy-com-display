<template>
    <b-modal id="streamDeletionConfirmModal"
             title="Suppression d'un fil d'actualités">
        <span v-if="stream">Voulez-vous supprimer "{{stream.name}}" ?</span>

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
</template>

<script>

export default {
    props: ['stream'],

    data() {
        return {};
    },

    methods: {
        closeStreamDeletionConfirmModal() {
            this.$root.$emit('hide::modal', 'streamDeletionConfirmModal');
        },

        confirmStreamDeletion() {
            this.$emit('confirmStreamDeletion', this.stream.id);
            this.closeStreamDeletionConfirmModal();
        }
    }
}

</script>

<style scoped>
</style>
