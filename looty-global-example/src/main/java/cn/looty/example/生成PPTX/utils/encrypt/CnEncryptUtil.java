package cn.looty.example.生成PPTX.utils.encrypt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对称加密和解密
 */
public class CnEncryptUtil {
	/**加密字典*/
	private static final Map<String, Integer> E = new HashMap<String, Integer>(20903);
	/**还原字典*/
	private static final Map<String, Integer> R = new HashMap<String, Integer>(20903);
	/**缓存数列*/
	private static final List<String> L = new ArrayList<String>(20903);
	
	static {
		//加载替换加密字典
		URL url =  CnEncryptUtil.class.getResource("/");
		FileInputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			inputStream = new FileInputStream(url.getPath()+ "ChinaGDic.txtbak");
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String str = null;
			while((str = bufferedReader.readLine()) != null)
			{
				String []strs = str.split("-");
				String key = strs[0];
				L.add(key);
				E.put(key, Integer.parseInt(strs[2]));
				R.put(key, Integer.parseInt(strs[3]));
			}
		} catch (IOException e) {

		} finally {
			try {
				if(inputStream!=null) {
					inputStream.close();
				}
				if(bufferedReader!=null) {
					bufferedReader.close();
				}
			} catch (Exception e2) {
			}
		}
	}
    
    /**中文替换加密*/
    public static String replaceCnEncode(String content, boolean doDesensitizat) {
    	if(content==null) {
    		return null;
    	}
    	if(doDesensitizat){
    		content = DesensitizationUtil.dealXingXing(content, 1, 1);
    	}
    	StringBuffer sb = new StringBuffer();
    	char[] arrays = content.toCharArray();
    	for (int i=0;i<arrays.length;i++) {
            char ti = arrays[i];
            if(Character.toString(ti).matches("[\\u4e00-\\u9fa5]")){
            	 String hex = Integer.toHexString(ti).toUpperCase();
                 Integer eIndex = E.get(hex);
                 String uStr = L.get(eIndex);
                 char letter = (char) Integer.parseInt(uStr, 16);
                 sb.append(letter);
            }else {
            	sb.append(ti);
            }
        }
    	return sb.toString();
    }
    
    /**中文替换解密*/
    public static String replaceCnDncode(String content) {
    	if(content==null) {
    		return null;
    	}
    	StringBuffer sb = new StringBuffer();
    	char[] arrays = content.toCharArray();
    	for (int i=0;i<arrays.length;i++) {
            char ti = arrays[i];
            if(Character.toString(ti).matches("[\\u4e00-\\u9fa5]")){
            	String hex = Integer.toHexString(ti).toUpperCase();
                Integer eIndex = R.get(hex);
                String uStr = L.get(eIndex);
                char letter = (char) Integer.parseInt(uStr, 16);
                sb.append(letter);
            }else {
            	sb.append(ti);
            }
        }
    	return sb.toString();
    }
}


