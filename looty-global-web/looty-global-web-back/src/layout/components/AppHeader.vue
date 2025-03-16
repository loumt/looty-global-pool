<template>
    <el-header>
        <!--图标-->
        <el-icon @click="handleMenuHideAndShow">
            <Expand v-show="sidebar.hide"/>
            <Fold v-show="!sidebar.hide"/>
        </el-icon>


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

<style lang="scss" scoped>
    $ly-header-back-color: #eeeeee;

    .el-header{
        display: flex;
        align-items: center;
        background-color: $ly-header-back-color;

        .el-dropdown{
            margin-left: auto;
            .el-avatar{
                background-color: $ly-header-back-color;
            }

            :hover{
                filter: drop-shadow(0 0 2em rgba(142, 210, 255, 0.67));
            }
        }
    }
</style>