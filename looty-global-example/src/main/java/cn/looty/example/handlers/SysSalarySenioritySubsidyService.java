package cn.looty.example.handlers;


import cn.looty.example.annotations.SysVersionTypeHandler;
import cn.looty.example.enums.SysVersionTypeEnum;

/**
 * @Filename: SysSalarySenioritySubsidyService
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 13:33
 */
@Service
@SysVersionTypeHandler(type = SysVersionTypeEnum.SENIORITY_SUBSIDY, tableName= "工龄补贴")
public class SysSalarySenioritySubsidyService extends SysVersionAbstractService{
    @Autowired
    private SysSalarySenioritySubsidyMapper mapper;

    @Override
    public SysVersionDetailDTO findVersionDetail(Long versionId) {
        SysVersionDetailDTO to = new SysVersionDetailDTO<SysSalarySenioritySubsidyTable>();
        to.setVersionId(versionId);
        to.setType(super.getType());
        to.setHasData(0);
        SysSalarySenioritySubsidyTable table = new SysSalarySenioritySubsidyTable();
        table.setTableName(super.getTableName());
        SysSalarySenioritySubsidyData subsidy = mapper.selectOne(SalaryTool.lambdaQuery(SysSalarySenioritySubsidyData.class).eq(SysSalarySenioritySubsidyData::getVersionId, versionId));
        if(subsidy == null) return to;

        to.setHasData(1);
        BeanUtils.copyProperties(subsidy, table);
        to.setTable(table);
        table.setSize(1);
        return to;
    }

    @Override
    public void updateVersionDetail(Long id, BaseTableRow row) {
        SysSalarySenioritySubsidyTableRow updateRow = (SysSalarySenioritySubsidyTableRow) row;

        SysSalarySenioritySubsidyData existData = mapper.selectOne(SalaryTool.lambdaQuery(SysSalarySenioritySubsidyData.class, row.getId()));
        SalaryTool.check(existData == null, ResultCode.REFRESH);

        LambdaUpdateWrapper<SysSalarySenioritySubsidyData> lqw = SalaryTool.lambdaUpdate(SysSalarySenioritySubsidyData.class, row.getId())
                .set(SysSalarySenioritySubsidyData::getUpLimit, updateRow.getUpLimit())
                .set(SysSalarySenioritySubsidyData::getMax, updateRow.getMax())
                .set(SysSalarySenioritySubsidyData::getAmount, updateRow.getAmount());

        int update = mapper.update(null, lqw);
        if(update == 0) throw new SalaryBusinessException(ResultCode.ERROR);
    }


    @Override
    public void removeVersionDetail(Long id) {
        SysSalarySenioritySubsidyData existData = mapper.selectOne(SalaryTool.lambdaQuery(SysSalarySenioritySubsidyData.class, id));
        SalaryTool.check(existData == null, ResultCode.REFRESH);
        int remove = mapper.deleteById(id);
        SalaryTool.check(remove == 0, ResultCode.ERROR);
    }

    @Override
    public void addVersionDetail(BaseTableRow row) {}
}

