package utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;

import jws.Logger;
import sun.misc.BASE64Encoder;

public class AliAPIUtil {
	/*
     * 计算MD5+BASE64
     */
    public static String MD5Base64(String s) {
        if (s == null)
            return null;
        String encodeStr = "";
        byte[] utfBytes = s.getBytes();
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(utfBytes);
            byte[] md5Bytes = mdTemp.digest();
            BASE64Encoder b64Encoder = new BASE64Encoder();
            encodeStr = b64Encoder.encode(md5Bytes);
        } catch (Exception e) {
            throw new Error("Failed to generate MD5 : " + e.getMessage());
        }
        return encodeStr;
    }
    /*
     * 计算 HMAC-SHA1
     */
    public static String HMACSha1(String data, String key) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = (new BASE64Encoder()).encode(rawHmac);
        } catch (Exception e) {
            throw new Error("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
    /*
     * 等同于javaScript中的 new Date().toUTCString();
     */
    public static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }	
    
    /*
     * 发送POST请求
     */
    public static <T> T sendPost(String url, String body, String ak_id, String ak_secret,Class<T> t) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        int statusCode = 200;
        try {
        	 
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "POST";
            String accept = "application/json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date + "\n"
                    + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Dataplus " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", content_type);
            conn.setRequestProperty("date", date);
            conn.setRequestProperty("Authorization", authHeader);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            statusCode = ((HttpURLConnection)conn).getResponseCode();
            if(statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection)conn).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            Logger.error(e, e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: "+ statusCode + "\nErrorMessage: " + result);
        }
        return  (T)new Gson().fromJson(result, t);
    }
    /*
     * GET请求
     */
    public static <T> T sendGet(String url, String ak_id, String ak_secret,Class<T> t) throws Exception {
        String result = "";
        BufferedReader in = null;
        int statusCode = 200;
        try { 
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "GET";
            String accept = "application/json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            // String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + "" + "\n" + content_type + "\n" + date + "\n" + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Dataplus " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", accept);
            connection.setRequestProperty("content-type", content_type);
            connection.setRequestProperty("date", date);
            connection.setRequestProperty("Authorization", authHeader);
            connection.setRequestProperty("Connection", "keep-alive");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            statusCode = ((HttpURLConnection)connection).getResponseCode();
            if(statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection)connection).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	 Logger.error(e, e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: "+ statusCode + "\nErrorMessage: " + result);
        }
        return  (T)new Gson().fromJson(result, t);
    }
    
    
    public static String accessKey = "HW77gOwWnQiwQIuB"; //用户ak
    public static String secretKey = "0N36kSmuIapg7352cX23fOGxUyXMoq"; // 用户ak_secret
    public static void main(String[] args) throws Exception {
    	testVerify();
    	//testAttribute();
    }
    
    private static void testVerify() throws Exception{
    	String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/face/verify";
    	String imgUrl1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509516018792&di=e528404899ccab71695290eaa246c7c1&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fbaike%2Fpic%2Fitem%2F08f790529822720ecbeba7e173cb0a46f21fab80.jpg";
        String imgUrl2="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509516070784&di=2b1014e70ed1db23d4e60358a259893f&imgtype=0&src=http%3A%2F%2Fimg.edu-hb.com%2FNewsImg%2Fnews%2F201551%2F20150902175130135.jpg";
        String body = "{\"type\": \"0\", \"image_url_1\":\""+imgUrl1+"\",\"image_url_2\":\""+imgUrl2+"\"}";
     	FaceVerfiyResponse resp = sendPost(url, body, accessKey, secretKey,FaceVerfiyResponse.class);
     	System.out.println(new Gson().toJson(resp));
    }
    
    private static void testAttribute() throws Exception{
     
    	String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/face/attribute";
    	String imgUrl = "https://ios.jugame.cn/pla/family.jpg";
        String body = "{\"type\": \"0\", \"image_url\":\""+imgUrl+"\"}";
        FaceAttributeResponse resp = sendPost(url, body, accessKey, secretKey,FaceAttributeResponse.class);
     	System.out.println(new Gson().toJson(resp));
    }
    
   public class FaceVerfiyResponse{
    	private String request_id;
    	private int errno; 
    	private int[] rectB;
    	private int[] rectA;
    	private float[] thresholds;
    	private float confidence;
		public String getRequest_id() {
			return request_id;
		}
		public void setRequest_id(String request_id) {
			this.request_id = request_id;
		}
		public int getErrno() {
			return errno;
		}
		public void setErrno(int errno) {
			this.errno = errno;
		}
		public int[] getRectB() {
			return rectB;
		}
		public void setRectB(int[] rectB) {
			this.rectB = rectB;
		}
		public int[] getRectA() {
			return rectA;
		}
		public void setRectA(int[] rectA) {
			this.rectA = rectA;
		}
		public float[] getThresholds() {
			return thresholds;
		}
		public void setThresholds(float[] thresholds) {
			this.thresholds = thresholds;
		}
		public float getConfidence() {
			return confidence;
		}
		public void setConfidence(float confidence) {
			this.confidence = confidence;
		} 
    }
   public class FaceAttributeResponse{
	   private int errno;
	   private String err_msg;
	   private String request_id;
	   private int face_num;
	   private int[] face_rect;
	   private float[] face_prob;
	   private float[] pose;
	   private int landmark_num;
	   private float[] landmark;
	   private float[] iris;
	   private int[] gender;
	   private int[] age;
	   private int[] expression;
	   private int[] glass;
	   private int dense_fea_len;
	   private float[] dense_fea;
	public int getErrno() {
		return errno;
	}
	public void setErrno(int errno) {
		this.errno = errno;
	}
	public String getErr_msg() {
		return err_msg;
	}
	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public int getFace_num() {
		return face_num;
	}
	public void setFace_num(int face_num) {
		this.face_num = face_num;
	}
	public int[] getFace_rect() {
		return face_rect;
	}
	public void setFace_rect(int[] face_rect) {
		this.face_rect = face_rect;
	}
	public float[] getFace_prob() {
		return face_prob;
	}
	public void setFace_prob(float[] face_prob) {
		this.face_prob = face_prob;
	}
	public float[] getPose() {
		return pose;
	}
	public void setPose(float[] pose) {
		this.pose = pose;
	}
	public int getLandmark_num() {
		return landmark_num;
	}
	public void setLandmark_num(int landmark_num) {
		this.landmark_num = landmark_num;
	}
	public float[] getLandmark() {
		return landmark;
	}
	public void setLandmark(float[] landmark) {
		this.landmark = landmark;
	}
	public float[] getIris() {
		return iris;
	}
	public void setIris(float[] iris) {
		this.iris = iris;
	}
	public int[] getGender() {
		return gender;
	}
	public void setGender(int[] gender) {
		this.gender = gender;
	}
	public int[] getAge() {
		return age;
	}
	public void setAge(int[] age) {
		this.age = age;
	}
	public int[] getExpression() {
		return expression;
	}
	public void setExpression(int[] expression) {
		this.expression = expression;
	}
	public int[] getGlass() {
		return glass;
	}
	public void setGlass(int[] glass) {
		this.glass = glass;
	}
	public int getDense_fea_len() {
		return dense_fea_len;
	}
	public void setDense_fea_len(int dense_fea_len) {
		this.dense_fea_len = dense_fea_len;
	}
	public float[] getDense_fea() {
		return dense_fea;
	}
	public void setDense_fea(float[] dense_fea) {
		this.dense_fea = dense_fea;
	}
	   
	   
   }
}
