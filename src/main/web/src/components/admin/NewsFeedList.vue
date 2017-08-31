<template>
    <div class="newsfeed-list">
        <h3>Flux d'actualités disponibles</h3>
        <div v-if="newsFeedList">
            <table class="newsfeed-list-table">
                <tr v-for="newsFeed in newsFeedList">
                    <td class="newsfeed-name">
                        {{newsFeed.name}}
                    </td>
                    <td>
                        <b-button variant="outline-danger" @click="deleteNewsFeed(newsFeed.id)">
                            <i class="fa fa-trash"></i>
                        </b-button>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</template>

<script>
import CmsService from '../../services/CmsService.js'

export default {
    data() {
        return {
            newsFeedList: null
        }
    },

    mounted() {
        this.getNewsFeedList();

        /*this.refreshId = window.setInterval(() => {
            this.getNewsFeedList();
        }, 1500);*/
    },

    beforeDestroy() {
        if (this.refreshId) {
            window.clearInterval(this.refreshId);
        }
    },

    methods: {
        getNewsFeedList() {
            return CmsService.getNewsFeedList().then((newsFeedList) => {
                this.newsFeedList = newsFeedList;
            });
        },

        deleteNewsFeed(newsFeedId) {
            return CmsService.deleteNewsFeed(newsFeedId).then(() => {
                return this.getNewsFeedList();
            })
        }
    }
}

</script>

<style scoped>
.newsfeed-list {
    margin-top: 10px;
    margin-bottom: 10px;
}

.newsfeed-list-table {
    width: 90%;
}

.newsfeed-list-table tr {
    border: 1px solid rgba(0, 0, 0, 0.2);;
}

.newsfeed-list-table tr:nth-child(odd) {
    background-color: #f7f7f9;
}

.newsfeed-list-table td {
    padding: 4px;
}

td.newsfeed-name {
    width: 80%;
}

</style>