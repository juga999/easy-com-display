<template>
    <div class="clock">
        <div class="clock-today">
            <span class="clock-day">{{currentDay}}</span>
            <br>
            <span class="clock-date">{{currentDate}}</span>
            <br>
            <span class="clock-month">{{currentMonth}}</span>
        </div>

        <div class="clock-time">
            <span class="clock-time-hours">{{currentHours}}</span>
            <span class="clock-time-separator">:</span>
            <span class="clock-time-minutes">{{currentMinutes}}</span>
        </div>
    </div>
</template>

<script>
const DAYS = new Array("DIM", "LUN", "MAR", "MER", "JEU", "VEN", "SAM");
const MONTHS = new Array("JAN", "FEV", "MAR", "AVR", "MAI", "JUN", "JUL", "AOU", "SEP", "OCT", "NOV", "DEC");

export default {
    data() {
        return {
            currentDay: '',
            currentDate: '',
            currentMonth: '',
            currentHours: '',
            currentMinutes: '',
        }
    },

    mounted() {
        this.refresh();
        this.refreshId = window.setInterval(() => this.refresh(), 1000);
    },

    beforeDestroy() {
        if (this.refreshId) {
            window.clearInterval(this.refreshId);
        }
    },

    methods: {
        refresh() {
            var date = new Date();
            this.getDate(date);
            this.getTime(date);
        },

        getDate(date) {
            this.currentDay = DAYS[date.getDay()];
            this.currentDate = date.getDate();
            this.currentMonth = MONTHS[date.getMonth()];
        },

        getTime(date) {
            let minutes = date.getMinutes();
            if (minutes < 10) {
                minutes = '0' + minutes;
            }
            this.currentMinutes = minutes;

            let hours = date.getHours();
            if (hours < 10) {
                hours = '0' + hours;
            }
            this.currentHours = hours;
        },
    }
}

</script>

<style scoped>
.clock {
    color: #c6c6c6;
}

.clock > * {
    vertical-align:middle;
}

.clock-today {
    display: inline-block;
    text-align: center;
    line-height: 0.8;
}

.clock-day {
    font-size: 1.7em;
}

.clock-date {
    font-size: 2.8em;
    color: white;
}

.clock-month {
    font-size: 1.7em;
}

.clock-time {
    display: inline-block;
    margin-left: 40%;
}

.clock-time-hours {
    color: white;
    font-size: 2em;
}

.clock-time-minutes {
    font-size: 2em;
}

</style>
