<template>
    <div class="location">
        <b-card header="<i class='fa fa-cloud'></i> Emplacement">
            <form @submit.prevent="saveLocation">
                <div class="form-group">
                    <label for="locationName">Ville</label>
                    <input type="text" v-model="location.name" class="form-control" id="locationName">
                </div>
                <div class="form-group">
                    <label for="forecastUrl">Site internet de la météo de la ville</label>
                    <input type="text" v-model="location.weatherForecastUrl" class="form-control" id="forecastUrl">
                </div>

                <b-alert :show="saveError != null" variant="danger">
                    {{saveError}}
                </b-alert>

                <div class="centered-btn-wrapper">
                    <b-button type="submit" class="save-btn"
                              :disabled="!location.name || !location.weatherForecastUrl"
                              variant="primary">
                        Enregistrer
                    </b-button>
                </div>
            </form>
        </b-card>
    </div>
</template>

<script>
import {locationService} from '@/services/LocationService.js'

export default {
    data() {
        return {
            saveError: null,
            location: {name: null, weatherForecastUrl: null}
        }
    },

    mounted() {
        locationService.getLocation().then((result) => {
            if (result) {
                this.location.name = result.name;
                this.location.weatherForecastUrl = result.weatherForecastUrl;
            }
        });
    },

    methods: {
        saveLocation() {
            this.saveError = null;
            locationService.setLocation(this.location).then((result) => {
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
