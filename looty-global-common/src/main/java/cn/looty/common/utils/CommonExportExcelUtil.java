package cn.looty.common.utils;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Filename: CommonExportExcelUtil
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-20 16:52
 */
public class CommonExportExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExportExcelUtil.class);

    @Data
    static class Bank{
        private String name;
        private BigDecimal limitCredit;
        private String principal;
        private String contact;

        private List<Account> accounts;
    }

    @Data
    static class Account{
        private String account;
        private String accountTypeName;
        private String serialNo;
        private BigDecimal balance;
        private String contact;
        private String companyName;
    }


    public static void example(HttpServletResponse response){
        String fileName = "示例";

        List<Bank> banks = Lists.newArrayList();

        Bank bank = new Bank();
        bank.setContact("示例");
        bank.setName("示例");
        bank.setPrincipal("示例");
        bank.setLimitCredit(new BigDecimal(1000.12));

        Account account = new Account();
        account.setAccount("示例");
        account.setAccountTypeName("示例");
        account.setSerialNo("示例");
        account.setContact("示例");
        account.setCompanyName("示例");
        account.setBalance(new BigDecimal(100.23));

        bank.setAccounts(Lists.newArrayList(account));
        banks.add(bank);

        exportBank(fileName, banks, response);
    }

    public static void exportBank(String fileName, List<Bank> banks, HttpServletResponse response) {
        response.reset();
        try {
            fileName += ".xls";
            String fileNameURL = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileNameURL + ";filename*=utf-8''" + fileNameURL);
            //创建一个WorkBook,对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("银行");
            HSSFFont font = wb.createFont();
            font.setBold(true);

            HSSFCellStyle titleLeftStyle = wb.createCellStyle();
            titleLeftStyle.setAlignment(HorizontalAlignment.LEFT);
            titleLeftStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleLeftStyle.setFont(font);

            HSSFCellStyle titleCenterStyle = wb.createCellStyle();
            titleCenterStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleCenterStyle.setFont(font);

            HSSFCellStyle titleRightStyle = wb.createCellStyle();
            titleRightStyle.setAlignment(HorizontalAlignment.RIGHT);
            titleRightStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleRightStyle.setFont(font);

            HSSFCellStyle leftStyle = wb.createCellStyle();
            leftStyle.setAlignment(HorizontalAlignment.LEFT);
            leftStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            HSSFCellStyle centerStyle = wb.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);
            centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            HSSFCellStyle rightStyle = wb.createCellStyle();
            rightStyle.setAlignment(HorizontalAlignment.RIGHT);
            rightStyle.setVerticalAlignment(VerticalAlignment.CENTER);


            int rowIndex = 0;
            if (!CollectionUtils.isEmpty(banks)) {
                List<CellRangeAddress> mergedRow = new ArrayList<>();
                int bankMax = banks.size();
                int bankIndex = 1;
                for (Bank bank : banks) {
                    HSSFRow titleRow = sheet.createRow(rowIndex);
                    titleRow.setHeight((short) 400);
                    mergedRow.add(new CellRangeAddress(rowIndex, rowIndex, 0, 2));
                    HSSFCell bankCell = titleRow.createCell(0);
                    bankCell.setCellValue(StringUtil.isEmpty(bank.getName()) ? "" : bank.getName());
                    bankCell.setCellStyle(titleLeftStyle);
                    mergedRow.add(new CellRangeAddress(rowIndex, rowIndex, 3, 4));
                    HSSFCell limitCreditCell = titleRow.createCell(3);
                    limitCreditCell.setCellValue(bank.getLimitCredit() == null? "" : bank.getLimitCredit().setScale(2, RoundingMode.HALF_UP).toPlainString());
                    limitCreditCell.setCellStyle(titleLeftStyle);

                    rowIndex++;
                    HSSFRow subtitleRow = sheet.createRow(rowIndex);
                    subtitleRow.setHeight((short) 400);
                    mergedRow.add(new CellRangeAddress(rowIndex, rowIndex, 0, 2));
                    HSSFCell bankPrincipalCell = subtitleRow.createCell(0);
                    bankPrincipalCell.setCellValue(StringUtil.isEmpty(bank.getPrincipal()) ? "-" : bank.getPrincipal());
                    bankPrincipalCell.setCellStyle(titleLeftStyle);
                    mergedRow.add(new CellRangeAddress(rowIndex, rowIndex, 3, 4));
                    HSSFCell contactCell = subtitleRow.createCell(3);
                    contactCell.setCellValue(StringUtil.isEmpty(bank.getContact()) ? "-" : bank.getContact());
                    contactCell.setCellStyle(titleLeftStyle);

                    rowIndex++;
                    HSSFRow headRow = sheet.createRow(rowIndex);
                    headRow.setHeight((short) 400);
                    HSSFCell titleCell0 = headRow.createCell(0);
                    titleCell0.setCellValue("序号");
                    titleCell0.setCellStyle(titleCenterStyle);
                    sheet.setColumnWidth(0, 2000);
                    HSSFCell titleCell1 = headRow.createCell(1);
                    titleCell1.setCellValue("账户");
                    titleCell1.setCellStyle(titleLeftStyle);
                    sheet.setColumnWidth(1, 5000);
                    HSSFCell titleCell2 = headRow.createCell(2);
                    titleCell2.setCellValue("主体公司");
                    titleCell2.setCellStyle(titleLeftStyle);
                    sheet.setColumnWidth(2, 4000);
                    HSSFCell titleCell3 = headRow.createCell(3);
                    titleCell3.setCellValue("账户类型");
                    titleCell3.setCellStyle(titleCenterStyle);
                    sheet.setColumnWidth(3, 3000);
                    HSSFCell titleCell4 = headRow.createCell(4);
                    titleCell4.setCellValue("可用余额");
                    titleCell4.setCellStyle(titleRightStyle);
                    sheet.setColumnWidth(4, 4000);

                    if (!CollectionUtils.isEmpty(bank.getAccounts())) {
                        for (Account account : bank.getAccounts()) {
                            rowIndex++;
                            HSSFRow dataRow = sheet.createRow(rowIndex);
                            dataRow.setHeight((short) 400);

                            HSSFCell serialNoCell = dataRow.createCell(0);
                            serialNoCell.setCellValue(StringUtil.isEmpty(account.getSerialNo()) ? "" : account.getSerialNo());
                            serialNoCell.setCellStyle(centerStyle);

                            HSSFCell bankAccountCell = dataRow.createCell(1);
                            bankAccountCell.setCellValue(StringUtil.isEmpty(account.getAccount()) ? "" : account.getAccount());
                            bankAccountCell.setCellStyle(leftStyle);

                            HSSFCell companyCell = dataRow.createCell(2);
                            companyCell.setCellValue(StringUtil.isEmpty(account.getCompanyName()) ? "" : account.getCompanyName());
                            companyCell.setCellStyle(leftStyle);

                            HSSFCell typeCell = dataRow.createCell(3);
                            typeCell.setCellValue(StringUtil.isEmpty(account.getAccountTypeName()) ? "" : account.getAccountTypeName());
                            typeCell.setCellStyle(centerStyle);

                            HSSFCell balanceCell = dataRow.createCell(4);
                            balanceCell.setCellValue(account.getBalance() == null? "" : account.getBalance().setScale(2, RoundingMode.HALF_UP).toPlainString());
                            balanceCell.setCellStyle(rightStyle);
                        }
                    }
                    if (bankIndex < bankMax) {
                        rowIndex++;
                        mergedRow.add(new CellRangeAddress(rowIndex, rowIndex, 0, 4));
                        HSSFRow emptyRow = sheet.createRow(rowIndex);
                        emptyRow.setHeight((short) 400);
                    }
                    bankIndex++;
                    rowIndex++;
                }
                for (CellRangeAddress cellRangeAddress : mergedRow) {
                    sheet.addMergedRegion(cellRangeAddress);
                }
            }

            //将文件输出
            OutputStream outPutStream = response.getOutputStream();
            wb.write(outPutStream);
            outPutStream.flush();
            outPutStream.close();
        } catch (Exception e) {
            logger.info("导出Excel失败！");
            logger.info(e.getMessage());
        }
    }
}
