<template>
    <div class="stream-view-wrapper">
        <div v-if="playlist" class="frame-container" :style="frameContainerStyle">
        </div>
        <div class="right-panel">
            <div class="clock-wrapper">
                <stream-clock></stream-clock>
            </div>
            <div class="separator">
            </div>
            <div class="weather-wrapper">
                <weather></weather>
            </div>
        </div>
        <div class="footer">
            <news-ticker></news-ticker>
        </div>
    </div>
</template>

<script>
import Q from 'q'

import { cmsService } from '@/services/CmsService.js';
import { playlistService } from '@/services/PlaylistService.js';

import Clock from './Clock.vue'
import NewsTicker from './NewsTicker.vue'
import Weather from './Weather.vue'

export default {
    components: {
        'stream-clock': Clock,
        'news-ticker': NewsTicker,
        'weather': Weather
    },

    data() {
        return {
            playlist: null,
            streams: [],
            currentStreamIndex: 0,
            currentFrameIndex: 0,
            frameContainerStyle: {
                position: 'absolute',
                top: '10px',
                left: '10px',
                bottom: '80px',
                width: '76%',
                'box-shadow': '5px 5px 20px 2px #323232',
                'background-image': 'none',
                'background-repeat': 'no-repeat',
                'background-size': '100% 100%',
                border: 'solid 1px #c6c6c6'
            }
        };
    },

    mounted() {
        this.refresh();
    },

    beforeDestroy() {
        if (this.timeoutId) {
            window.clearTimeout(this.timeoutId);
        }
    },

    methods: {
        getPlaylist() {
            return playlistService.getPlaylist().then((playlist) => {
                this.playlist = playlist;
                return this.playlist;
            });
        },

        refresh() {
            if (this.currentStreamIndex === 0 && this.currentFrameIndex === 0) {
                this.getPlaylist().then((playlist) => {
                    this.streams = playlist.streams;
                    if (this.streams.length > 0) {
                        let delay = this._advanceSlideShow();
                        this.timeoutId = window.setTimeout(() => this.refresh(), delay);
                    } else {
                        this.timeoutId = window.setTimeout(() => this.refresh(), 1500);
                    }
                });
            } else {
                let delay = this._advanceSlideShow();
                this.timeoutId = window.setTimeout(() => this.refresh(), delay);
            }
        },

        _advanceSlideShow() {
            let stream = this.streams[this.currentStreamIndex];
            let frame = cmsService.getFrame(stream, this.currentFrameIndex);
            if (frame) {
                this.frameContainerStyle['background-image'] = frame.url;
                ++this.currentFrameIndex;
                if (this.currentFrameIndex === stream.frames.length) {
                    this.currentFrameIndex = 0;
                    ++this.currentStreamIndex;
                }
                if (this.currentStreamIndex === this.streams.length) {
                    this.currentStreamIndex = 0;
                }
            } else {
                this.currentFrameIndex = 0;
                this.currentFrameIndex = 0;
            }

            let delay = frame && frame.delay || stream.timing;

            return delay * 1000;
        }
    }
}

</script>

<style scoped>

.stream-view-wrapper {
    position: absolute;
    top: 0;
    left: 0;
    bottom:0;
    right: 0;
    background-image: url('/static/background.jpg');
    background-size: cover;
}

.right-panel {
    float: right;
    width: 20%;
    display: inline-block;
    height: 50%;
    margin-top: 10px;
    margin-right: 24px;
    background: linear-gradient(to bottom, rgba(0,0,0,0.7), rgba(0,0,0,0.4));
    box-shadow: 5px 5px 20px 2px #323232;
}

.separator {
    width: 100%;
    border-bottom: 1px solid rgba(255,255,255,0.2);
}

.clock-wrapper {
    padding-top: 20px;
    padding-bottom: 20px;
    padding-left: 4px;
    padding-right: 4px;
}

.weather-wrapper {
    padding-top: 20px;
    padding-bottom: 20px;
    padding-left: 4px;
    padding-right: 4px;
}

.footer {
    position: absolute;
    bottom: 0;
    right: 0;
    left: 0;
    height: 64px;
    
    background: linear-gradient(to bottom, rgba(0,0,0,0.7), rgba(0,0,0,0.4));
}

</style>
