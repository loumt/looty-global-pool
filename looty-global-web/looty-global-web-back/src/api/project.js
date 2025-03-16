import request from '@/utils/request'


//project info
export function projectInfo() {
    return request({
        url: '/api/project',
        method: 'get'
    })
}