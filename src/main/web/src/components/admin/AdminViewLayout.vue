<template>
<b-container fluid class="admin-view-container">
    <b-row>
        <b-col cols="2" class="admin-view-menu-container">
            <b-card no-body>
                <div slot="header" class="admin-view-title">Administration</div>
                <b-card-body class="admin-view-menu">
                    <b-list-group>
                        <b-list-group-item key="staging" to="/kiosk/playlist">
                            <i class="fa fa-photo"></i>&nbsp;&nbsp;Playlist
                        </b-list-group-item>
                        <b-list-group-item key="cms" to="/admin/cms">
                            <i class="fa fa-database"></i>&nbsp;&nbsp;Gestion du contenu
                        </b-list-group-item>
                        <b-list-group-item key="settings" to="/admin/settings">
                            <i class="fa fa-gear"></i>&nbsp;&nbsp;Configuration de l'afficheur
                        </b-list-group-item>
                    </b-list-group>
                </b-card-body>
            </b-card>
        </b-col>
        <b-col cols="9">
            <slot name="admin-view-content"></slot>
        </b-col>
    </b-row>
</b-container>
</template>

<script>
</script>

<style scoped>
.admin-view-container {
    margin-left: 0px;
}

.admin-view-menu-container {
    padding: 0px;
    height: 100vh;
}

.admin-view-menu-container .card {
    border: 1px solid #E55934;
    border-radius: 0.25rem;
}

.admin-view-menu-container .card-header {
    color: white;
    font-weight: bold;
    background: #E55934;
}

.admin-view-menu-container .card-body {
    padding: 4px;
}

.admin-view-menu {
    border: none;
}

.admin-view-title {
    text-align: center;
}
</style>
