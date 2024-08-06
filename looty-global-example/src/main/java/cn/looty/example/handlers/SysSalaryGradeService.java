package cn.looty.example.handlers;


import cn.looty.example.annotations.SysVersionTypeHandler;
import cn.looty.example.enums.SysVersionTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * @Filename: SysSalaryGradeService
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 13:32
 */
@Service
@SysVersionTypeHandler(type = SysVersionTypeEnum.SALARY_GRADE, tableName=  "事业单位管理人员（专技人员）薪级工资调整标准增资对照表")
public class SysSalaryGradeService extends SysVersionAbstractService{
    @Autowired
    private SysSalaryGradeMapper mapper;

    @Override
    public SysVersionDetailDTO findVersionDetail(Long versionId) {
        SysVersionDetailDTO to = new SysVersionDetailDTO<SysSalarySubsidyDataTable>();
        to.setVersionId(versionId);
        to.setType(super.getType());

        SysSalaryGradeDataTable table = new SysSalaryGradeDataTable();
        table.setTableName(super.getTableName());
        List<SysSalaryGradeDataTableRow> list = mapper.findList(versionId);
        table.setSize(list.size());
        Map<String, List<SysSalaryGradeDataTableRow>> groupList = list.stream().collect(Collectors.groupingBy(SysSalaryGradeDataTableRow::getTypeName));

        List<SysSalaryGradeDataTableGroup> groups = Lists.newArrayList();
        for (String typeName : groupList.keySet()) {
            SysSalaryGradeDataTableGroup group = new SysSalaryGradeDataTableGroup();
            group.setName(typeName);
            group.setRows(groupList.get(typeName));
            groups.add(group);
        }
        table.setGroups(groups);
        to.setTable(table);
        return to;
    }

    @Override
    public void updateVersionDetail(Long id, BaseTableRow row) {
        SysSalaryGradeDataTableRow updateRow = (SysSalaryGradeDataTableRow) row;

        SysSalaryGradeData existData = mapper.selectOne(SalaryTool.lambdaQuery(SysSalaryGradeData.class, row.getId()));
        if(existData == null) throw new SalaryBusinessException(ResultCode.REFRESH);

        LambdaUpdateWrapper<SysSalaryGradeData> lqw = SalaryTool.lambdaUpdate(SysSalaryGradeData.class, row.getId())
                .set(SysSalaryGradeData::getSalary, updateRow.getSalary());

        int update = mapper.update(null, lqw);
        if (update == 0) throw new SalaryBusinessException(ResultCode.ERROR);
    }

    @Override
    public void removeVersionDetail(Long id) {
        SysSalaryGradeData existData = mapper.selectOne(SalaryTool.lambdaQuery(SysSalaryGradeData.class, id));
        SalaryTool.check(existData == null, ResultCode.REFRESH);
        int remove = mapper.deleteById(id);
        SalaryTool.check(remove == 0, ResultCode.ERROR);
    }

    @Override
    public void addVersionDetail(BaseTableRow row) {
        SysSalaryGradeDataTableRow data = (SysSalaryGradeDataTableRow) row;

        SysSalaryGradeData existData = mapper.selectOne(SalaryTool.lambdaQuery(SysSalaryGradeData.class).eq(SysSalaryGradeData::getTypeId, data.getTypeId()).eq(SysSalaryGradeData::getTypeLevelId, data.getTypeLevelId()));
        SalaryTool.check(existData != null, ResultCode.SALARY_VERSION_DETAIL_EXIST);

        SysSalaryGradeData bean = new SysSalaryGradeData();
        BeanUtils.copyProperties(data, bean);
        SalaryTool.check(mapper.insert(bean) == 0, ResultCode.ERROR);
    }
}
