package cn.looty.example.handlers;

import cn.looty.example.annotations.SysVersionTypeHandler;
import cn.looty.example.enums.SysVersionTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Filename: SysGroupPersonalNatureService
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-31 14:59
 */
@Service
@SysVersionTypeHandler(type = SysVersionTypeEnum.GROUP_PERSONAL_NATURE, tableName = "群体人员性质标准数据表")
public class SysGroupPersonalNatureService extends SysVersionAbstractService {
    @Autowired
    private SysGroupPersonalNatureMapper mapper;

    @Override
    public SysVersionDetailDTO findVersionDetail(Long versionId) {
        SysVersionDetailDTO to = new SysVersionDetailDTO<SysGroupPersonalNatureTable>();
        to.setVersionId(versionId);
        to.setType(super.getType());
        SysGroupPersonalNatureTable table = new SysGroupPersonalNatureTable();
        table.setTableName(super.getTableName());
        List<SysGroupPersonalNatureData> listResults = mapper.selectList(SalaryTool.lambdaQuery(SysGroupPersonalNatureData.class).eq(SysGroupPersonalNatureData::getVersionId, versionId));
        if (CollectionUtil.isEmpty(listResults)) {
            to.setHasData(0);
            return to;
        }
        List<SysGroupPersonalNatureTableGroup> groups = Lists.newArrayList();
        for (SysGroupPersonalNatureData listResult : listResults) {
            SysGroupPersonalNatureTableGroup g = new SysGroupPersonalNatureTableGroup();
            SysGroupPersonalNatureTableRow r = new SysGroupPersonalNatureTableRow();
            BeanUtils.copyProperties(listResult, r);
            g.setRows(Lists.newArrayList(r));
            groups.add(g);
        }
        table.setGroups(groups);
        table.setSize(listResults.size());
        to.setHasData(1);
        to.setTable(table);
        return to;
    }

    @Override
    public void updateVersionDetail(Long id, BaseTableRow row) {
        SysGroupPersonalNatureTableRow updateRow = (SysGroupPersonalNatureTableRow) row;

        SysGroupPersonalNatureData existData = mapper.selectOne(SalaryTool.lambdaQuery(SysGroupPersonalNatureData.class, row.getId()));
        SalaryTool.check(existData == null, ResultCode.REFRESH);

        LambdaUpdateWrapper<SysGroupPersonalNatureData> lqw = SalaryTool.lambdaUpdate(SysGroupPersonalNatureData.class, row.getId())
                .set(SysGroupPersonalNatureData::getHousingFundPercent, updateRow.getHousingFundPercent())
                .set(SysGroupPersonalNatureData::getSocietyInsurance, updateRow.getSocietyInsurance())
                .set(SysGroupPersonalNatureData::getRemark, updateRow.getRemark());

        int update = mapper.update(null, lqw);
        if (update == 0) throw new SalaryBusinessException(ResultCode.ERROR);
    }


    @Override
    public void removeVersionDetail(Long id) {
        SysGroupPersonalNatureData existData = mapper.selectOne(SalaryTool.lambdaQuery(SysGroupPersonalNatureData.class, id));
        SalaryTool.check(existData == null, ResultCode.REFRESH);
        int remove = mapper.deleteById(id);
        SalaryTool.check(remove == 0, ResultCode.ERROR);
    }

    @Override
    public void addVersionDetail(BaseTableRow row) {
        SysGroupPersonalNatureTableRow data = (SysGroupPersonalNatureTableRow) row;

        SysGroupPersonalNatureData existData = mapper.selectOne(SalaryTool.lambdaQuery(SysGroupPersonalNatureData.class).eq(SysGroupPersonalNatureData::getGroupId, data.getGroupId()).eq(SysGroupPersonalNatureData::getNatureId, data.getNatureId()));
        SalaryTool.check(existData != null, ResultCode.SALARY_VERSION_DETAIL_EXIST);

        SysGroupPersonalNatureData bean = new SysGroupPersonalNatureData();
        BeanUtils.copyProperties(data, bean);
        SalaryTool.check(mapper.insert(bean) == 0, ResultCode.ERROR);
    }
}
