package cn.looty.example.enums;

/**
 * @Filename: SysVersionTypeEnum
 * @Description:
 * @Version: 1.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-08-02 10:12
 */
public enum SysVersionTypeEnum {
    SALARY_SUBSIDY(1, ""),
    COUNTRY_ENGAGE(2, ""),
    GROUP_PERSONAL_NATURE(3, ""),
    SENIORITY_SUBSIDY(4, ""),
    SALARY_GRADE(4, ""),
    ;


    private int code;
    private String name;

    SysVersionTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }}
