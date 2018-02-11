package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.UUID;

import jws.Jws;

public class FileUtil {

	public static String genFileName(String prefix){
		return UUID.randomUUID().toString()+prefix;
	}
	
	/**
	 * 文件存储相对地址
	 * @param file
	 * @param type
	 * @return
	 * @throws Exception 
	 */
	public static String storeFile(File tmpFile,int type) throws Exception{
		if(!tmpFile.exists()){
			throw new Exception("源文件不存在");
		}
		String tmpFileName = tmpFile.getName();
		String prefix=tmpFileName.substring(tmpFileName.lastIndexOf("."));
		char split = File.separatorChar;
		String destName="";
		switch (type){
			case 1:
				destName = split+"files"+split+"record"+split+genFileName(prefix);
				break;
			case 2:
				destName = split+"files"+split+"photo"+split+genFileName(prefix);
				break;
			case 3:
				destName = split+"files"+split+"suggest"+split+genFileName(prefix);
				break;
			default:
				destName = "";
		}
		String destFilePath = Jws.applicationPath.getPath()+destName;
		File destFile = new File(destFilePath);
		if(destFile.exists()){
			throw new Exception("文件存在重名["+destFilePath+"]");
		}
		if(!destFile.createNewFile()){
			throw new Exception("文件创建失败["+destFilePath+"]");
		}
		forTransfer(tmpFile,destFile);
		return destName;
	}
	
	/**
	 * 文件拷贝
	 * @param f1
	 * @param f2
	 * @return
	 * @throws Exception
	 */
	public static long forTransfer(File f1,File f2) throws Exception{
		 FileInputStream in = null;
		 FileOutputStream out = null;
		 FileChannel inC = null;
		 FileChannel outC = null;
		try{
			long time=System.currentTimeMillis();
	        in=new FileInputStream(f1);
	        out=new FileOutputStream(f2);
	        inC=in.getChannel();
	        outC=out.getChannel();
	        inC.transferTo(0, inC.size(), outC);
	        return System.currentTimeMillis() - time;
        }finally{
        	if(in!=null)in.close();
        	if(out!=null)out.close();
        	if(inC!=null)inC.close();
        	if(outC!=null)outC.close();
        }
    }
}
