<template>
    <div class="weather">
        <div v-if="weatherForecast && weatherForecast.period">
            <div class="day-period">
                {{translatedPeriod.toUpperCase()}}
            </div>
            <div class="temperature-wrapper">
                <span class="temperature">{{weatherForecast.temperature}}</span>
                <span class="felt-temperature">{{weatherForecast.feltTemperature}}</span>
            </div>
            <div class="sky-and-rain">
                <div class="sky">{{weatherForecast.sky}}</div>
                <div class="rain">{{weatherForecast.rain}}</div>
            </div>
        </div>
    </div>
</template>

<script>
import { cmsService } from '@/services/CmsService.js';

export default {
    data() {
        return {
            weatherForecast: null
        };
    },

    mounted() {
        this.refresh();
        this.refreshId = window.setInterval(() => this.refresh(), 2000);
    },

    beforeDestroy() {
        if (this.refreshId) {
            window.clearInterval(this.refreshId);
        }
    },

    computed: {
        translatedPeriod() {
            if (this.weatherForecast.period === 'morning') {
                return 'Matin';
            } else if (this.weatherForecast.period === 'afternoon') {
                return 'Après-midi';
            } else {
                return 'Soirée';
            }
        }
    },

    methods: {
        refresh() {
            cmsService.getWeatherForecast().then((forecast) => {
                this.weatherForecast = forecast;
            })
        }
    }
}
</script>

<style scoped>
.weather {
    color: #c6c6c6;
}

.day-period {
    font-family: 'RobotoBold';
    font-size: 1.7em;
}

.temperature {
    color: #f1d104;
    font-size: 2.5em;
}

.felt-temperature {
}

.sky-and-rain {
    color: white;
    text-align: center;
}

.rain {
}

</style>
