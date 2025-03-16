<template>
    <div class="pagination-container">
        <el-pagination  background
                        layout="sizes, prev, pager, next, jumper"
                        :total="total"
                        :page-size="pageSize"
                        :page-sizes="pageSizes"
                        :current-page="pageNo"
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
        />
    </div>
</template>

<script lang="ts" setup name="pagination">
    const props = defineProps({
        total: {
            required: true,
            type: Number,
            default: 0
        },
        isShow: {
            type: Boolean,
            default: true
        },
        pageNo: {
            type: Number,
            default: 1
        },
        pageSize: {
            type: Number,
            default: 5
        },
        pageSizes: {
            type: Array,
            default() {
                return [5, 10, 20, 50]
            }
        }
    })


    const emit = defineEmits();
    const pageNo = computed({
        get() {
            return props.pageNo
        },
        set(val) {
            emit('update:pageNo', val)
        }
    })
    const pageSize = computed({
        get() {
            return props.pageSize
        },
        set(val){
            emit('update:pageSize', val)
        }
    })

    function handleSizeChange(val){
        pageSize.set(val)
    }

    function handleCurrentChange(val){
        pageNo.set(val)
    }
</script>

<style scoped name="pagination">
    .pagination-container {
        background: #fff;
        padding: 32px 16px;
    }
</style>