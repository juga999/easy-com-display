<template>
    <div class="location">
        <h3>Emplacement</h3>
        <b-card header="<i class='fa fa-cloud'></i> Météo">
            <form @submit.prevent="saveLocation">
                <div class="form-group">
                    <label for="locationName">Ville</label>
                    <input type="text" v-model="locationName" class="form-control" id="locationName">
                </div>
                <div class="form-group">
                    <label for="forecastUrl">Site internet de la météo de la ville</label>
                    <input type="text" v-model="forecastUrl" class="form-control" id="forecastUrl">
                </div>

                <b-alert :show="saveError != null" variant="danger">
                    {{saveError}}
                </b-alert>

                <div class="centered-btn-wrapper">
                    <b-button type="submit" class="save-btn"
                              :disabled="!locationName || !forecastUrl"
                              variant="primary">
                        Enregistrer
                    </b-button>
                </div>
            </form>
        </b-card>
    </div>
</template>

<script>
import LocationService from '@/services/LocationService.js'

export default {
    data() {
        return {
            saveError: null,
            locationName: null,
            forecastUrl: null,
        }
    },

    mounted() {
        LocationService.getLocation().then((result) => {
            if (result) {
                this.locationName = result.name;
                this.forecastUrl = result.weatherForecastUrl;
            }
        });
    },

    methods: {
        saveLocation() {
            this.saveError = null;
            LocationService.setLocation(this.locationName, this.forecastUrl).then((result) => {
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
.location {
    margin-top: 10px;
    margin-bottom: 10px;
}
</style>
