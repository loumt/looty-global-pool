package cn.looty.example.handlers;

import cn.looty.example.annotations.SysVersionTypeHandler;
import cn.looty.example.enums.SysVersionTypeEnum;
import cn.looty.example.models.BaseTableRow;
import cn.looty.example.models.SysSalarySubsidyDataTable;
import cn.looty.example.models.SysSalarySubsidyDataTableGroup;
import cn.looty.example.models.SysSalarySubsidyDataTableRow;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Filename: SysSalarySubsidyService
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 13:30
 */
@Service
@SysVersionTypeHandler(type = SysVersionTypeEnum.SALARY_SUBSIDY, tableName = "基础性绩效工资")
public class SysSalarySubsidyService extends SysVersionAbstractService {

    @Autowired
    private SysSalarySubsidyMapper mapper;


    @Override
    public SysVersionDetailDTO findVersionDetail(Long versionId) {
        SysVersionDetailDTO to = new SysVersionDetailDTO<SysSalarySubsidyDataTable>();
        to.setVersionId(versionId);
        to.setType(super.getType());

        SysSalarySubsidyDataTable table = new SysSalarySubsidyDataTable();
        table.setTableName(super.getTableName());
        List<SysSalarySubsidyDataTableRow> list = mapper.findList(versionId);
        table.setSize(list.size());
        Map<String, List<SysSalarySubsidyDataTableRow>> groupList = list.stream().collect(Collectors.groupingBy(SysSalarySubsidyDataTableRow::getTypeName));

        List<SysSalarySubsidyDataTableGroup> groups = Lists.newArrayList();
        for (String typeName : groupList.keySet()) {
            SysSalarySubsidyDataTableGroup group = new SysSalarySubsidyDataTableGroup();
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
        SysSalarySubsidyDataTableRow updateRow = (SysSalarySubsidyDataTableRow) row;
        if (mapper.selectOne(SalaryTool.lambdaQuery(SysSalarySubsidyData.class, row.getId())) == null)
            throw new SalaryBusinessException(ResultCode.REFRESH);
        LambdaUpdateWrapper<SysSalarySubsidyData> lqw = SalaryTool.lambdaUpdate(SysSalarySubsidyData.class, row.getId())
                .set(SysSalarySubsidyData::getSalarySubsidy, updateRow.getSalarySubsidy())
                .set(SysSalarySubsidyData::getLiveSubsidy, updateRow.getLiveSubsidy());
        if (mapper.update(null, lqw) == 0) throw new SalaryBusinessException(ResultCode.ERROR);
    }

    @Override
    public void addVersionDetail(BaseTableRow row) {
        SysSalarySubsidyDataTableRow data = (SysSalarySubsidyDataTableRow) row;

        SysSalarySubsidyData existData = mapper.selectOne(SalaryTool.lambdaQuery(SysSalarySubsidyData.class).eq(SysSalarySubsidyData::getTypeId, data.getTypeId()).eq(SysSalarySubsidyData::getTypeLevelId, data.getTypeLevelId()));
        SalaryTool.check(existData != null, ResultCode.SALARY_VERSION_DETAIL_EXIST);

        SysSalarySubsidyData bean = new SysSalarySubsidyData();
        BeanUtils.copyProperties(data, bean);
        SalaryTool.check(mapper.insert(bean) == 0, ResultCode.ERROR);
    }

    @Override
    public void removeVersionDetail(Long id) {
        SalaryTool.check(mapper.selectOne(SalaryTool.lambdaQuery(SysSalarySubsidyData.class, id)) == null, ResultCode.REFRESH);
        SalaryTool.check(mapper.deleteById(id) == 0, ResultCode.ERROR);
    }
}
