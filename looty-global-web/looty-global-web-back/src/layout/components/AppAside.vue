
<template>
    <el-aside>
        <el-menu router unique-opened :collapse="sidebarHide">
            <a href="/" class="logo">
                <img src="@/assets/main-icon.png" alt="管理后台"/>
                <h1>管理后台</h1>
            </a>

            <el-sub-menu index="1">
                <template #title>
                    <el-icon><User /></el-icon> <span>权限管理</span>
                </template>

                <el-menu-item index="/menus">
                    <el-icon><User /></el-icon> <span>菜单列表</span>
                </el-menu-item>

                <el-menu-item index="/resources">
                    <el-icon><User /></el-icon> <span>资源管理</span>
                </el-menu-item>

                <el-menu-item index="roles">
                    <el-icon><User /></el-icon> <span>角色列表</span>
                </el-menu-item>
            </el-sub-menu>


            <el-menu-item index="/users">
                <el-icon><User /></el-icon> <span>用户管理</span>
            </el-menu-item>

            <el-menu-item index="/systems">
                <el-icon><User /></el-icon> <span>系统设置</span>
            </el-menu-item>


            <el-sub-menu index="2">
                <template #title>
                    <el-icon><User /></el-icon> <span>广告管理</span>
                </template>

                <el-menu-item index="advertises">
                    <el-icon><User /></el-icon> <span>广告列表</span>
                </el-menu-item>

                <el-menu-item index="attachments">
                    <el-icon><Crop /></el-icon> <span>素材管理</span>
                </el-menu-item>
            </el-sub-menu>
        </el-menu>
    </el-aside>
</template>


<style lang="scss" scope>
    .el-aside {
        backgroud-color: #eee;

        /* 这样就设置了元素的高度为视口高度的 100% */
        height: 100vh;
        width: auto;

        .el-menu {
            background-color :#eee;
            border-right: none;
            width: 200px;

            &.el-menu--collapse {
                width: 60px;
                & h1{
                    display: none;
                }
            }

            .logo{
                display: flex;
                justify-content: center;
                align-items: center;
                height: 60px;
                text-decoration: none;
                color: #000000;
                img {
                    width: 32px;
                    height: 32px;
                }
            }
        }
    }
</style>

<script lang="ts" setup>
    import useAppStore from '@/store/modules/app'
    const sidebarHide = computed(()=>(useAppStore().sidebar.hide))
</script>