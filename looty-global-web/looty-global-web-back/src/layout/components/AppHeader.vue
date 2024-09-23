<template>
    <el-header>
        <!--图标-->
        <el-icon @click="handleMenuHideAndShow">
            <Expand v-show="sidebar.hide"/>
            <Fold v-show="!sidebar.hide"/>
        </el-icon>

        <!--面包屑-->
        <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">homepage</el-breadcrumb-item>
            <el-breadcrumb-item>
                <a href="/">promotion management</a>
            </el-breadcrumb-item>
            <el-breadcrumb-item>promotion list</el-breadcrumb-item>
            <el-breadcrumb-item>promotion detail</el-breadcrumb-item>
        </el-breadcrumb>

        <!--下拉菜单-->
        <el-dropdown>
            <span class="el-dropdown-link">
                 <el-avatar :size="50" :src="userInfo.header" />
            </span>
            <template #dropdown>
                <el-dropdown-menu>
                    <el-dropdown-item>用户姓名</el-dropdown-item>
                    <el-dropdown-item>退出</el-dropdown-item>
                </el-dropdown-menu>
            </template>
        </el-dropdown>
    </el-header>
</template>


<script lang="ts" setup>
    import useAppStore from '@/store/modules/app'
    const sidebar = computed(()=>(useAppStore().sidebar))
    const userInfo = computed(()=>(useAppStore().userInfo))

    function handleMenuHideAndShow() {
        useAppStore().showAndHideMenu()
    }
</script>

<style lang="scss" scope>
    $ly-header-back-color: #eeeeee;

    .el-header{
        display: flex;
        align-items: center;
        background-color: $ly-header-back-color;

        .el-breadcrumb{
            margin-left: 20px;
        }

        .el-dropdown{
            margin-left: auto;
            .el-avatar{
                background-color: $ly-header-back-color;
            }
        }
    }
</style>