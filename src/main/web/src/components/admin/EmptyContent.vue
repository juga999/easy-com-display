<template>
<div class="empty-content-container">
    {{message}}
</div>
</template>

<script>
export default {
    props: ['message'],

    data() {
        return {};
    }
}
</script>

<style scoped>
.empty-content-container {
    background-color: white;
    border: solid 1px rgba(0, 0, 0, 0.1);
    padding: 10px;
    text-align: center;
    font-size: 2.2em;
    color: rgba(0,0,0,0.4);
}
</style>