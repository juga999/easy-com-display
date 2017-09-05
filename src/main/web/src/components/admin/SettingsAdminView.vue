<template>
<admin-view-layout>
    <b-container slot="admin-view-content">
        <b-row>
            <b-col cols="12">
                <power-management></power-management>
            </b-col>
        </b-row>
        <b-row>
            <b-col cols="12">
                <this-location></this-location>
            </b-col>
        </b-row>
    </b-container>
</admin-view-layout>
</template>

<script>
import AdminViewLayout from '@/components/admin/AdminViewLayout.vue'

import PowerManagement from '@/components/admin/PowerManagement.vue'
import ThisLocation from '@/components/admin/Location.vue'

export default {
    components: {
        'admin-view-layout': AdminViewLayout,
        'power-management': PowerManagement,
        'this-location': ThisLocation
    }
}
</script>