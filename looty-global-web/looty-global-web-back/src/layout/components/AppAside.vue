
<template>
    <el-aside>
        <el-menu router unique-opened :collapse="sidebarHide">
            <a href="/" class="logo">
                <img src="@/assets/main-icon.png" alt="管理后台"/>
                <h1>管理后台</h1>
            </a>


            <el-menu-item index="/index">
                <el-icon><User /></el-icon> <span>首页统计</span>
            </el-menu-item>

            <el-sub-menu index="ARTICLE">
                <template #title>
                    <el-icon><User /></el-icon> <span>文章管理</span>
                </template>

                <el-menu-item index="/article/article">
                    <el-icon><User /></el-icon> <span>文章列表</span>
                </el-menu-item>

                <el-menu-item index="/article/resources">
                    <el-icon><User /></el-icon> <span>素材管理</span>
                </el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="PERMISSION">
                <template #title>
                    <el-icon><User /></el-icon> <span>权限管理</span>
                </template>

                <el-menu-item index="/permission/menus">
                    <el-icon><User /></el-icon> <span>菜单列表</span>
                </el-menu-item>

                <el-menu-item index="/permission/resources">
                    <el-icon><User /></el-icon> <span>资源管理</span>
                </el-menu-item>

                <el-menu-item index="/permission/roles">
                    <el-icon><User /></el-icon> <span>角色列表</span>
                </el-menu-item>
            </el-sub-menu>


            <el-menu-item index="/users">
                <el-icon><User /></el-icon> <span>用户管理</span>
            </el-menu-item>


            <el-sub-menu index="ADVERTISES">
                <template #title>
                    <el-icon><User /></el-icon> <span>广告管理</span>
                </template>

                <el-menu-item index="/advertises/advertises">
                    <el-icon><User /></el-icon> <span>广告列表</span>
                </el-menu-item>

                <el-menu-item index="/advertises/attachments">
                    <el-icon><Crop /></el-icon> <span>素材管理</span>
                </el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="SYSTEM">
                <template #title>
                    <el-icon><User /></el-icon> <span>系统设置</span>
                </template>

                <el-menu-item index="/system/properties">
                    <el-icon><User /></el-icon> <span>全局属性</span>
                </el-menu-item>
            </el-sub-menu>


            <el-menu-item index="/about">
                <el-icon><User /></el-icon> <span>关于我们</span>
            </el-menu-item>
        </el-menu>
    </el-aside>
</template>


<style lang="scss" scoped>
    .el-aside {
        background-color: #eee;

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

                :hover{
                    filter: drop-shadow(0 0 2rem rgb(255, 0, 8));
                }
            }
        }
    }
</style>

<script lang="ts" setup>
    import useAppStore from '@/store/modules/app'
    const sidebarHide = computed(()=>(useAppStore().sidebar.hide))
</script>