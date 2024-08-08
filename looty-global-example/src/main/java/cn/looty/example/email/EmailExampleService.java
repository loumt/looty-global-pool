package cn.looty.example.email;

import cn.hutool.core.util.ZipUtil;
import cn.looty.example.enums.EmailOriginTypeEnum;
import com.google.common.collect.Lists;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @Classname EmailExampleService
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/8 23:55
 */

public class EmailExampleService {
    protected static final String FOLDER = "C:\\code";
    private EmailOriginTypeEnum type = EmailOriginTypeEnum.ORIGIN_ONE;

    public void start(){
        try {
            /**
             * 附件落地
             */
            receiveAttachment(type.getEmail(), type.getAuthCode());

            /**
             * 处理附件
             */
            handingAttachment();

            /**
             * 备份附件
             */
            backupAttachment();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void backupAttachment() {
    }


    public void handingAttachment() throws IOException {
        String folder = type.name();
        Path path = Paths.get(folder);

        Files.list(path).forEach(p -> {
            File unzip = ZipUtil.unzip(new File(p.toString()), Charset.forName("GBK"));
            parseDataFile(unzip);
        });
    }

    protected boolean patchFileName(String fileName) {
        /**
         * 1.文件zip结尾
         * 2.文件带日期 yyyy-MM-dd
         */
        return fileName.lastIndexOf(".zip") != -1 && fileName.matches(".*\\d{4}\\d{2}\\d{2}.zip$");
    }


    private void parseDataFile(File dataFileFolder){
        for (File file : dataFileFolder.listFiles()) {
            String fileName = file.getName();
            String fullFilePath = file.getAbsolutePath();
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getName());
        }
    }


    protected void receiveAttachment(String emailForm, String authCode) throws MessagingException, IOException {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3");
        props.setProperty("mail.pop3.port", "110");
        props.setProperty("mail.pop3.host", "SMTP.qq.com");
        Session session = Session.getInstance(props);
        Store store = session.getStore("pop3");
        store.connect(emailForm, authCode);
        Folder folder = store.getFolder("INBOX");
        folder.open(2);
        Message[] messages = folder.getMessages();

        loadingAttachment(messages);

        folder.close(true);
        store.close();
    }

    protected boolean isContainAttachment(Part part) throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart)part.getContent();
            int partCount = multipart.getCount();

            for(int i = 0; i < partCount; ++i) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp == null || !disp.equalsIgnoreCase("attachment") && !disp.equalsIgnoreCase("inline")) {
                    if (bodyPart.isMimeType("multipart/*")) {
                        flag = isContainAttachment(bodyPart);
                    } else {
                        String contentType = bodyPart.getContentType();
                        if (contentType.contains("application")) {
                            flag = true;
                        }

                        if (contentType.contains("name")) {
                            flag = true;
                        }
                    }
                } else {
                    flag = true;
                }

                if (flag) {
                    break;
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part)part.getContent());
        }

        return flag;
    }


    protected void loadingAttachment(Message... messages) throws IOException, MessagingException {
        for(Message msg: messages){
            MimeMessage mimeMessage = (MimeMessage)msg;
//            System.out.println("主题: " + getSubject(mimeMessage));
//            System.out.println("发件人: " + getFrom(mimeMessage));
//            System.out.println("收件人：" + getReceiveAddress(mimeMessage, null));
//            System.out.println("发送时间：" + getSentDate(mimeMessage, null));
//            System.out.println("邮件大小：" + msg.getSize() * 1024 + "kb");
            boolean isContainerAttachment = isContainAttachment(mimeMessage);
            if(isContainerAttachment){
                saveAttachment(mimeMessage, FOLDER);
            }
        }
    }


    private void saveAttachment(Part part, String destDir) throws UnsupportedEncodingException, MessagingException, FileNotFoundException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart)part.getContent();
            int partCount = multipart.getCount();

            for(int i = 0; i < partCount; ++i) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp == null || !disp.equalsIgnoreCase("attachment") && !disp.equalsIgnoreCase("inline")) {
                    if (bodyPart.isMimeType("multipart/*")) {
                        saveAttachment(bodyPart, destDir);
                    } else {
                        String contentType = bodyPart.getContentType();
                        if (contentType.contains("name") || contentType.contains("application")) {
                            if(patchFileName(bodyPart.getFileName())){
                                saveFile(bodyPart.getInputStream(), destDir,decodeText(bodyPart.getFileName()));
                            }
                        }
                    }
                } else {
                    InputStream is = bodyPart.getInputStream();
                    saveFile(is, destDir, decodeText(bodyPart.getFileName()));
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part)part.getContent(), destDir);
        }

    }


    public static String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {
        StringBuilder receiveAddress = new StringBuilder();
        Address[] addresss = null;
        if (type == null) {
            addresss = msg.getAllRecipients();
        } else {
            addresss = msg.getRecipients(type);
        }

        if (addresss != null && addresss.length >= 1) {
            Address[] var4 = addresss;
            int var5 = addresss.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Address address = var4[var6];
                InternetAddress internetAddress = (InternetAddress)address;
                receiveAddress.append(internetAddress.toUnicodeString()).append(",");
            }

            receiveAddress.deleteCharAt(receiveAddress.length() - 1);
            return receiveAddress.toString();
        } else {
            throw new MessagingException("没有收件人!");
        }
    }

    public static String getSentDate(MimeMessage msg, String pattern) throws MessagingException {
        Date receivedDate = msg.getSentDate();
        if (receivedDate == null) {
            return "";
        } else {
            if (pattern == null || "".equals(pattern)) {
                pattern = "yyyy年MM月dd日 E HH:mm ";
            }

            return (new SimpleDateFormat(pattern)).format(receivedDate);
        }
    }


    public static boolean isSeen(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.SEEN);
    }


    public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part)part.getContent(), content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart)part.getContent();
            int partCount = multipart.getCount();

            for(int i = 0; i < partCount; ++i) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart, content);
            }
        }

    }


    public void saveFile(InputStream is, String destDir, String fileName) throws FileNotFoundException, IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        String folder = type.name();
        String folderPath = destDir + File.separator + folder;
        File directory = new File(folderPath);
        if (!directory.exists()){
            directory.mkdirs();
        }

        File file = new File(folderPath + File.separator + fileName);
//        System.out.println("文件路径 => " + file.getAbsolutePath());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        boolean var5 = true;

        int len;
        while((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }

        bos.close();
        bis.close();
    }


    public String decodeText(String encodeText) throws UnsupportedEncodingException {
        return encodeText != null && !"".equals(encodeText) ? MimeUtility.decodeText(encodeText) : "";
    }

    public String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
        return MimeUtility.decodeText(msg.getSubject());
    }

    public String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        String from = "";
        Address[] froms = msg.getFrom();
        if (froms.length < 1) {
            throw new MessagingException("没有发件人!");
        } else {
            InternetAddress address = (InternetAddress)froms[0];
            String person = address.getPersonal();
            if (person != null) {
                person = MimeUtility.decodeText(person) + " ";
            } else {
                person = "";
            }

            from = person + "<" + address.getAddress() + ">";
            return from;
        }
    }

    public  List<String> readFileText(File file){
        List<String> lines = Lists.newArrayList();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                lines.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return lines;
    }

}
