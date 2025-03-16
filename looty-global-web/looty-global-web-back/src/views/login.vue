<template>
    <div class="login">
        <el-form :model="form" label-width="auto" style="max-width: 600px" label-position="top">
            <h2 class="login-title">登录</h2>
            <el-form-item label="手机号">
                <el-input v-model="form.phone" />
            </el-form-item>
            <el-form-item label="密码">
                <el-input v-model="form.password" />
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="login">登录</el-button>
            </el-form-item>
        </el-form>
    </div>

</template>

<script setup>
    const form = reactive({
        phone: "",
        password: ''
    })

    function login(){

    }
</script>

<style lang="scss" scoped>
    .login{
        background-color: #cccccc;
        display: flex;
        height: 100vh;
        justify-content: center;
        align-items: center;

        .el-form{
            width: 300px;
            background-color: #eeeeee;
            padding: 30px;
            border-radius: 10px;

            .login-title{
                display: flex;
                justify-content: center;
            }

            .el-form-items{
                margin-top: 10px;
            }

            .el-button{
                width: 100vh;
            }
        }
    }
</style>