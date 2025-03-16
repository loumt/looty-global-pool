import request from '@/utils/request'

//获取博客列表
export function getArticleList(data){
    return request({
        url: '/api/article/list',
        method: 'post',
        data
    })
}

