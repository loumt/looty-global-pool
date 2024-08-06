package cn.looty.example.handlers;


import cn.looty.example.annotations.SysVersionTypeHandler;
import cn.looty.example.enums.SysVersionTypeEnum;
import cn.looty.example.models.BaseTableRow;
import cn.looty.example.models.SysCountryEngageDataTable;
import cn.looty.example.models.SysCountryEngageDataTableGroup;
import cn.looty.example.models.SysCountryEngageDataTableRow;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Filename: SysCountryEngageService
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 13:32
 */
@Service
@SysVersionTypeHandler(type = SysVersionTypeEnum.COUNTRY_ENGAGE, tableName= "岗位工资调整标准增资对照表")
public class SysCountryEngageService extends SysVersionAbstractService {
    @Autowired
    private SysCountryEngageMapper mapper;

    @Override
    public SysVersionDetailDTO findVersionDetail(Long versionId) {
        SysVersionDetailDTO to = new SysVersionDetailDTO<SysCountryEngageDataTable>();
        to.setVersionId(versionId);
        to.setType(super.getType());

        SysCountryEngageDataTable table = new SysCountryEngageDataTable();
        table.setTableName(super.getTableName());
        List<SysCountryEngageDataTableRow> list = mapper.findList(versionId);
        table.setSize(list.size());
        Map<String, List<SysCountryEngageDataTableRow>> groupList = list.stream().collect(Collectors.groupingBy(SysCountryEngageDataTableRow::getTypeName));

        List<SysCountryEngageDataTableGroup> groups = Lists.newArrayList();
        for (String typeName : groupList.keySet()) {
            SysCountryEngageDataTableGroup group = new SysCountryEngageDataTableGroup();
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
        SysCountryEngageDataTableRow updateRow = (SysCountryEngageDataTableRow) row;

        SysCountryEngageData existData = mapper.selectOne(SalaryTool.lambdaQuery(SysCountryEngageData.class, row.getId()));
        SalaryTool.check(existData == null, ResultCode.REFRESH);

        LambdaUpdateWrapper<SysCountryEngageData> lqw = SalaryTool.lambdaUpdate(SysCountryEngageData.class, row.getId())
                .set(SysCountryEngageData::getSalary, updateRow.getSalary())
                .set(SysCountryEngageData::getJobLevel, updateRow.getJobLevel());

        int update = mapper.update(null, lqw);
        SalaryTool.check(update == 0, ResultCode.ERROR);
    }

    @Override
    public void removeVersionDetail(Long id) {
        SysCountryEngageData existData = mapper.selectOne(SalaryTool.lambdaQuery(SysCountryEngageData.class, id));
        SalaryTool.check(existData == null, ResultCode.REFRESH);
        int remove = mapper.deleteById(id);
        SalaryTool.check(remove == 0, ResultCode.ERROR);
    }

    @Override
    public void addVersionDetail(BaseTableRow row) {
        SysCountryEngageDataTableRow data = (SysCountryEngageDataTableRow) row;

        SysCountryEngageData existData = mapper.selectOne(SalaryTool.lambdaQuery(SysCountryEngageData.class).eq(SysCountryEngageData::getTypeId, data.getTypeId()).eq(SysCountryEngageData::getTypeLevelId, data.getTypeLevelId()));
        SalaryTool.check(existData != null, ResultCode.SALARY_VERSION_DETAIL_EXIST);

        SysCountryEngageData bean = new SysCountryEngageData();
        BeanUtils.copyProperties(data, bean);
        SalaryTool.check(mapper.insert(bean) == 0, ResultCode.ERROR);
    }
}

