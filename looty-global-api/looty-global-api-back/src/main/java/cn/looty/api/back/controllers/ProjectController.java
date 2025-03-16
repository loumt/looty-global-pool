package cn.looty.api.back.controllers;

import cn.looty.common.base.BaseController;
import cn.looty.common.config.ProjectConfig;
import cn.looty.common.result.ApiResult;
import cn.looty.common.result.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Filename: ProjectController
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-21 14:36
 */
@RestController
@RequestMapping("/api/project")
public class ProjectController extends BaseController {

    @Autowired
    private ProjectConfig projectConfig;

    @GetMapping
    public ApiResult project(){
        return success(projectConfig);
    }
}
