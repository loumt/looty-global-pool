
<template>
    <div class="common-layout">
        <el-container>
            <AppAside/>
            <el-container class="ly-main-container">
                <AppHeader/>
                <el-main>Main</el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script lang="ts" setup>
    import AppAside from "./components/AppAside"
    import AppHeader from "./components/AppHeader"
</script>

<style lang="scss" scope>
    .ly-main-container{
        flex-direction: column;
    }
</style>