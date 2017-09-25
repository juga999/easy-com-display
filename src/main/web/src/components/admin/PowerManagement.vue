<template>
    <div class="admin-power-savings">
        <h3>Gestion de l'énergie</h3>
        <b-card header="<i class='fa fa-tv'></i> Téléviseur">
            <form @submit.prevent="saveTvHours">
                <div class="form-group">
                    <label for="wakeupTime">Heure d'allumage</label>
                    <input type="time" v-model="tvTimes.wakeupTime" class="form-control" id="wakeupTime">
                </div>
                <div class="form-group">
                    <label for="sleepTime">Heure de mise en veille</label>
                    <input type="time" v-model="tvTimes.sleepTime" class="form-control" id="sleepTime">
                </div>

                <b-alert :show="saveError != null" variant="danger">
                    {{saveError}}
                </b-alert>

                <div class="centered-btn-wrapper">
                    <b-button type="submit" class="save-btn"
                              :disabled="!tvTimes.wakeupTime || !tvTimes.sleepTime"
                              variant="primary">
                        Enregistrer
                    </b-button>
                </div>
            </form>
        </b-card>
    </div>
</template>

<script>
import { powerManagementService } from '@/services/PowerManagementService.js'

export default {
    data() {
        return {
            saveError: null,
            tvTimes: {wakeupTime: null, sleepTime: null}
        }
    },

    mounted() {
        powerManagementService.getTvTimes().then((result) => {
            if (result) {
                this.tvTimes.wakeupTime = result.wakeupTime;
                this.tvTimes.sleepTime = result.sleepTime;
            }
        });
    },

    methods: {
        saveTvHours() {
            this.saveError = null;
            powerManagementService.setTvTimes(this.tvTimes).then((result) => {
                if (!result) {
                    this.saveError = 'La sauvegarde a échoué';
                }
            }, (result) => {
                this.saveError = 'La sauvegarde a échoué';
            });
        }
    }
}
</script>

<style scoped>
.admin-power-savings {
    margin-top: 10px;
    margin-bottom: 10px;
}
</style>
