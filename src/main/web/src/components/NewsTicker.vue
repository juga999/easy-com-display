<template>
    <div class="news-ticker">
        <div class="news-source">{{currentSource}}</div><div class="news-title">{{currentNews}}</div>
    </div>
</template>

<script>
import CmsService from '../services/CmsService.js'

export default {
    data() {
        return {
            newsSources: [],
            currentSource: '',
            currentSourceNews: [],
            currentNews: ''
        };
    },

    mounted() {
        this.refresh();
        this.refreshId = window.setInterval(() => this.refresh(), 6000);
    },

    beforeDestroy() {
        if (this.refreshId) {
            window.clearInterval(this.refreshId);
        }
    },

    methods: {
        refresh() {
            if (this.newsSources.length === 0 && this.currentSourceNews.length === 0) {
                CmsService.getAggregatedNewsFeed().then((newsFeeds) => {
                    
                    this.newsPerFeed = newsFeeds && newsFeeds.newsPerFeed || {};
                    this.newsSources = Object.keys(this.newsPerFeed);
                    if (this.newsSources.length > 0) {
                        this.currentSource = this.newsSources.splice(0, 1)[0];
                        this.currentSourceNews = this.newsPerFeed[this.currentSource];
                        if (this.currentSourceNews.length > 0) {
                            this.currentNews = this.currentSourceNews.splice(0, 1)[0];
                        }
                    }
                });
            } else if (this.currentSourceNews.length > 0) {
                this.currentNews = this.currentSourceNews.splice(0, 1)[0];
            }

            if (this.currentSourceNews.length === 0) {
                this.newsSources.splice(0, 1)[0];
                if (this.newsSources.length > 0) {
                    this.currentSource = this.newsSources[0];
                }
            }
        }
    }
}

</script>

<style scoped>
.news-ticker {
    width: 100%;
    padding: 2px;
    display: flex;
    justify-content:center;
    align-content:center;
    flex-direction:column;
}

.news-source {
    font-family: 'RobotoRegular';
    color: #c6c6c6;
    font-size: 1.1em;
}

.news-title {
    color: white;
    font-family: 'RobotoBold';
    font-size: 1.4em;
}
</style>
