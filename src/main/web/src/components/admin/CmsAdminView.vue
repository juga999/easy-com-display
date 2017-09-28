<template>
<admin-view-layout>
    <b-container fluid slot="admin-view-content">
        <b-row>
            <b-col cols="12">
                <streams-admin></streams-admin>
            </b-col>
        </b-row>
        <b-row>
            <b-col cols="12">
                <newsfeeds-admin></newsfeeds-admin>
            </b-col>
        </b-row>
        <b-row>
            <b-col cols="6">
                <this-location></this-location>
            </b-col>
        </b-row>
    </b-container>
</admin-view-layout>
</template>

<script>
import AdminViewLayout from '@/components/admin/AdminViewLayout.vue'

import StreamsAdmin from '@/components/admin/StreamsAdmin.vue'
import NewsFeedsAdmin from '@/components/admin/NewsFeedsAdmin.vue'
import ThisLocation from '@/components/admin/Location.vue'

export default {
    components: {
        'admin-view-layout': AdminViewLayout,
        'streams-admin': StreamsAdmin,
        'newsfeeds-admin': NewsFeedsAdmin,
        'this-location': ThisLocation
    },

    data() {
        return {};
    },
}
</script>

<style scoped>
</style>
