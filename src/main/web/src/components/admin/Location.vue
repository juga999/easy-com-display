<template>
    <div class="weather">
        <h3>Météo</h3>
        <b-card>
            <form @submit.prevent="saveLocation">
                <div class="form-group">
                    <label for="forecastUrl">Lien vers la page <a target="_blank" href="https://www.prevision-meteo.ch">Previsionmeteo.ch</a> de la ville</label>
                    <input type="text" v-model="location.weatherForecastUrl" class="form-control" id="forecastUrl">
                </div>

                <b-alert :show="saveError != null" variant="danger">
                    {{saveError}}
                </b-alert>

                <div class="centered-btn-wrapper">
                    <b-button type="submit" class="save-btn"
                              :disabled="!location.weatherForecastUrl"
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
            location: {weatherForecastUrl: null}
        }
    },

    mounted() {
        locationService.getLocation().then((result) => {
            if (result) {
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
.weather {
    margin-top: 10px;
    margin-bottom: 10px;
}
</style>
