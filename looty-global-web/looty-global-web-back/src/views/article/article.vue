<template>
    <el-table v-loading="dataLoading" :data="dataList" style="width: 100%">
        <el-table-column  align='center' prop="title" label="标题" width="120" />
        <el-table-column  align='center' prop="content" label="内容" width="300" />

        <el-table-column  align='center' label="完成进度" width="120">
            <template #default="scope">
                <el-progress :text-inside="true" :stroke-width="26" :percentage="scope.row.process"/>
            </template>
        </el-table-column>

        <el-table-column align='center' label="是否可见" width="120">
            <template #default="scope">
                <el-switch :model-value="scope.row.isVisible == 'NO'" disabled/>
            </template>
        </el-table-column>

        <el-table-column align='center' label="是否禁用" width="120">
            <template #default="scope">
                <el-switch :model-value="scope.row.isDisable == 'NO'" disabled/>
            </template>
        </el-table-column>

        <el-table-column align='center' label="是否删除" width="120">
            <template #default="scope">
                <el-switch :model-value="scope.row.isDelete == 'NO'" disabled/>
            </template>
        </el-table-column>

        <el-table-column label="创建时间" align="center" width="180">
            <template #default="scope">
                {{formatDate(scope.row.createTime, '{y}-{m}-{d} {h}:{m}:{s}')}}
            </template>
        </el-table-column>

        <el-table-column label="更新时间" align="center" width="180">
            <template #default="scope">
                {{formatDate(scope.row.updateTime, '{y}-{m}-{d} {h}:{m}:{s}')}}
            </template>
        </el-table-column>

        <el-table-column  align='center' fixed="right" label="操作" min-width="120">
            <template #default="scope">
                <el-tooltip content="详情" placement="top">
                    <el-button link type="primary" icon="Tickets" @click="handleDetail(scope.row)"></el-button>
                </el-tooltip>
                <el-tooltip content="编辑" placement="top">
                    <el-button link type="primary" icon="Edit" @click="handleEdit(scope.row)"></el-button>
                </el-tooltip>
                <el-tooltip content="删除" placement="top">
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"></el-button>
                </el-tooltip>
            </template>
        </el-table-column>
    </el-table>

    <pagination :total="dataTotal" :pageSize="pageSize" :pageNo="pageNo" @update:pageNo="updatePageNo" @update:pageSize="updatePageSize"/>
</template>

<script lang="ts" setup name="article">
    import {getArticleList} from '@/api/article'

    let dataList = ref([]);
    let dataTotal = ref(0);
    let dataLoading = ref(true);
    let pageNo = ref(1);
    let pageSize = ref(5);

    function initData(options){
        getArticleList(options).then(data => {
            dataList = data.records
            dataTotal = data.total
            loading(false)
        });
    }

    function loading(f = true){
        dataLoading.value = f
    }

    const handleDetail = row => {
        console.dir(row.id)
    }

    const handleEdit = row => {
        console.dir(row.id)
    }

    const handleDelete = row => {
        console.dir(row.id)
    }

    function updatePageSize(val){
        pageSize = val
        initData({pageNo: pageNo.value, pageSize: pageSize.value})
    }


    function updatePageNo(val){
        pageNo = val
        initData({pageNo: pageNo.value, pageSize: pageSize.value})
    }

    loading()
    initData({pageNo: pageNo.value, pageSize: pageSize.value})
</script>

<style lang="scss" scoped>

</style>