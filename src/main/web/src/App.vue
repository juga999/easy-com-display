<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app'
}
</script>

<style>
@font-face {
  font-family: 'RobotoRegular';
  src: url('./assets/fonts/Roboto-Regular.ttf') format('truetype');
}

@font-face {
  font-family: 'RobotoBold';
  src: url('./assets/fonts/Roboto-Bold.ttf') format('truetype');
}

#app {
  font-family: 'RobotoRegular';
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

h3 {
    color: #E55934;
    padding: 4px;
    border-bottom: solid 1px;
    font-size: medium;
}

.centered-btn-wrapper {
   text-align: center;
}

.navigation-pane {
    background-color: #E55934;
    padding-top: 20px;
    padding-left: 20px;
    height: 98vh;
}

.navigation-pane a {
    color: #1D3557;
    text-decoration: none;
}

.navigation-menu-entry-icon {
    margin-right: 8px;
}

</style>
