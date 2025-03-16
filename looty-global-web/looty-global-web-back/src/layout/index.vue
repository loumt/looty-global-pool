
<template>
    <div class="common-layout">
        <el-container>
            <AppAside/>
            <el-container class="ly-main-container">
                <AppHeader/>
                <el-main>
                    <el-scrollbar>
                        <RouterView/>
                    </el-scrollbar>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script lang="ts" setup>
    import AppAside from "./components/AppAside"
    import AppHeader from "./components/AppHeader"
</script>

<style lang="scss" scoped>
    .ly-main-container{
        flex-direction: column;
    }

</style>