package cn.looty.example.策略工厂模式.enums;

/**
 * @Filename: SysVersionTypeEnum
 * @Description:
 * @Version: 1.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-08-02 10:12
 */
public enum SysVersionTypeEnum {
    COUNTRY_ENGAGE(1, "岗位工资调整标准增资对照表"),
    PROFESSION(2, "基础性绩效工资"),
    SALARY_GRADE(3, "事业单位管理人员（专技人员）薪级工资调整标准增资对照表"),
    ;


    private int code;
    private String name;

    SysVersionTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }}
