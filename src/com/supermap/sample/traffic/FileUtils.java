/*
 * All rights reserved.
 */
package com.supermap.sample.traffic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 文件操作工具类
 * 
 * @since 1.0
 * 
 * @author Liudong
 * @version 2.0 Date:2003-11-2
 */
public class FileUtils {
	
	private static Log log = LogFactory.getLog(FileUtils.class);
	
	public static boolean StringStartWith(String str, char sep){
        try
        {
            return str.charAt(0) == sep;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    public static boolean StringEndWith(String str, char sep){
        try
        {
            return str.charAt(str.length() -1) == sep;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    /**
     * 链接两个路径,加上分隔字符
     * @param start String
     * @param end String
     * @return String
     */
    public static String CancatPath(String start, String end)
    {
        return CancatPath(start, end, File.separatorChar);
    }

	 /**
     * 链接两个路径,加上分隔字符
     * @param start String
     * @param end String
     * @param sep char
     * @return String
     */
    public static String CancatPath(String start, String end, char sep)
    {
        if(end == null)
            return start;

        boolean startendswithsep = StringEndWith(start, sep);
        boolean endstartswithsep = StringStartWith(end,sep);
        if(startendswithsep)
        {
            if(endstartswithsep)
            {
                return start + end.substring(1);
            }
            else
            {
                return start + end;
            }
        }
        else
        {
            if(endstartswithsep)
            {
                 return start + end;
            }
            else
                return start + sep + end;
        }
    }
    
	/**
	 * 将页面内容写入到文件
	 * @param content 页面内容
	 * @param encoding  编码
	 * @return
	 */
	public static boolean writePageToFile(String content, String filePath,
                                          String encoding) {
		boolean result=false;
		try{
			if (!makedirs(filePath)) {
				return false;
			}
			if (content != null) {
				org.apache.commons.io.FileUtils.writeStringToFile(new File(
						filePath), content, encoding);
				result=true;
			}else{
				new File(filePath).createNewFile();
			}
		}catch(Exception e){
			log.error("将内容写入页面时出错", e);
		}
		return result;
	}
	
	 /**
     * 判断路径目录是否创建，没有时创建它
     * @param filepath
     * @return
     */
	public static boolean makedirs(String filepath) {
		File file = new File(filepath);
		File parentdir = file.getParentFile();
		if (parentdir == null) {
			return false;
		}
		if (parentdir.isDirectory()) {
			return true;
		}
		return parentdir.mkdirs();
	}

	/**
	 * 封装文件拷贝操作
	 * 
	 * @param sourceFile
	 *            源文件路径
	 * @param targetFile
	 *            目标文件路径
	 * @return 返回操作成功标志。true:成功/false:失败
	 */
	public static boolean copyFile(String sourceFile, String targetFile) {
		makedirs(targetFile);
		File file1 = new File(sourceFile);
		File file2 = new File(targetFile);
		FileInputStream fis = null;
		FileOutputStream fos = null;

		boolean isOK = true;

		try {
			if (file1.exists()) {
				if (!file2.exists()) {
					file2.createNewFile();

				}
				fis = new FileInputStream(file1);
				byte[] buffer = new byte[(int) file1.length()];
				fis.read(buffer);
				fos = new FileOutputStream(file2);
				fos.write(buffer);
			} else {
				isOK = false;
			}
		} catch (Exception e) {
			System.out.println(e);
			isOK = false;
		} finally {
			try {
				fis.close();
			} catch (Exception ex) {
			}
			try {
				fos.close();
			} catch (Exception ex) {
			}
		}
		return isOK;
	}

	/**
	 * 使用nio进行快速的文件拷贝
	 * 
	 * @param in
	 *            输入文件
	 * @param out
	 *            输出文件
	 * @throws IOException
	 *             当源文件不存在的时候会抛出
	 */
	public static void copyLargeFile(String in, String out) throws IOException {
		File inFile = new File(in);
		File outFile = new File(out);
		copyLargeFile(inFile, outFile);
	}

	/**
	 * 使用nio进行快速的文件拷贝
	 * 
	 * @param in
	 *            输入文件
	 * @param out
	 *            输出文件
	 * @throws IOException
	 *             当源文件不存在的时候会抛出
	 */
	public static void copyLargeFile(File in, File out) throws IOException {
		if (!in.exists()) {
			throw new FileNotFoundException("源文件不存在");
		}
		if (!out.exists()) {
			out.createNewFile();
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel fcin = null;
		FileChannel fcout = null;

		try {
			fis = new FileInputStream(in);
			fos = new FileOutputStream(out);
			fcin = fis.getChannel();
			fcout = fos.getChannel();
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
			while (true) {
				buffer.clear();

				int r = fcin.read(buffer);

				if (r == -1) {
					break;
				}

				buffer.flip();

				fcout.write(buffer);
			}
		} catch (Exception ex) {
			throw new IOException("拷贝文件出现异常：" + ex.getMessage());
		} finally {
			try {
				fcin.close();
			} catch (Exception ex) {
			}
			try {
				fcout.close();
			} catch (Exception ex) {
			}
			try {
				fis.close();
			} catch (Exception ex) {
			}
			try {
				fos.close();
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * 读取文本文件到字符串
	 * 
	 * @param fileName
	 *            要读取的文本文件的路径
	 * @return 文本文件实际内容的字符串
	 */
	public static String readFile(String fileName, String encode) {
		if(encode == null || "".equals(encode))
			encode = "UTF-8";
		StringBuffer sb = new StringBuffer();
		Vector v = readFileByLine(fileName,encode);
		boolean isDataInVector = (v.size() > 0);

		if (isDataInVector) {
			for (int i = 0; i < v.size(); i++) {
				sb.append(v.elementAt(i));
				sb.append("\n");
			}
			return sb.toString();
		} else {
			return null;
		}
	}

	/**
	 * 逐行读取文本文件
	 * 
	 * @param fileName
	 *            要读取的文本文件的路径
	 * @return Vector结构的字符串数组
	 */
	private static Vector readFileByLine(String fileName, String encode) {
		String s;
		Vector v = new Vector();
		LineNumberReader lnr = null;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		
		try {
			fis = new FileInputStream(fileName);
			isr = new InputStreamReader(fis,encode);  //GBK替换成你文件内容使用的编码
			lnr = new LineNumberReader(isr);

			while ((s = lnr.readLine()) != null) {
				v.addElement(s);
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				isr.close();
				fis.close();
			} catch (Exception ex) {
			}
			try {
				lnr.close();
			} catch (Exception ex) {
			}
		}
		return v;
	}

	/**
	 * 写字符串到指定文件中,如果文件已经存在则直接覆盖
	 * 
	 * @param fileName
	 *            要写入文件的文件名
	 * @param src
	 *            要写入的字符串内容
	 */
	public static boolean writeStringToFile(String fileName, String src) {
		if (src == null) {
			src = "";

		}
		boolean isOk = true;
		FileWriter out = null;
		try {
			out = new FileWriter(fileName);
			out.write(src);
			out.close();
		} catch (IOException e) {
			isOk = false;
			e.printStackTrace();
		} catch (Exception e) {
			isOk = false;
			e.printStackTrace();
		} finally {
			out = null;
		}
		return isOk;
	}

	/**
	 * 创建目录
	 * 
	 * @param path
	 *            路径
	 */
	public static void makeDirs(String path) {
		File file = new File(path);
		try {
			file.mkdirs();
		} catch (Exception e) {
		} finally {
			file = null;
		}
	}


	public static void inputstreamtofile(InputStream ins, File file){
		OutputStream os;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 *            文件名
	 * @return 操作成功--true/否则--false
	 */
	public static boolean deleteFile(File file) {
		boolean isOk = false;
		if (file.isFile()) {
			try {
				isOk = file.delete();
			} catch (Exception e) {
				isOk = false;
			}
		}
		return isOk;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 操作成功--true/否则--false
	 */
	public static boolean deleteFile(String fileName) {
		return deleteFile(new File(fileName));
	}

	/**
	 * 删除目录,该目录下的所有内容会被一起删除
	 * 
	 * @param dirPath
	 *            目录的路径
	 * @return 操作成功--true/否则--false
	 */
	public static boolean deleteDir(File dirPath) {
		boolean isOk = true;
		if (dirPath.isDirectory()) {
			try {
				deleteDirs(dirPath);
			} catch (Exception e) {
				isOk = false;
			}
		}
		return isOk;
	}

	/**
	 * 删除目录,该目录下的所有内容会被一起删除
	 * 
	 * @param path
	 *            目录的路径
	 * @return 操作成功--true/否则--false
	 */
	public static boolean deleteDir(String path) {
		return deleteDir(new File(path));
	}

	/**
	 * 删除多级目录
	 * 
	 * @param path
	 */
	private static void deleteDirs(File path) {
		File[] files = null;
		if (path.isDirectory()) {
			path.delete();
			files = path.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					deleteDirs(files[i]);
				}
			}
		}
		path.delete();
	}

	/**
	 * 写对象数据到指定的文件
	 * 
	 * @param obj
	 *            对象实例
	 * @param path
	 *            文件路径
	 */
	public static void writeObjectToFile(Object obj, String path) {
		writeObjectToFile(obj, new File(path));
	}

	/**
	 * 写对象数据到指定的文件
	 * 
	 * @param obj
	 *            对象实例
	 * @param path
	 *            文件对象
	 */
	public static void writeObjectToFile(Object obj, File path) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(path);
			writeObjectToFile(obj, out);
		} catch (Exception ex) {
		} finally {
			try {
				out.close();
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * 写对象数据到指定的输出流
	 * 
	 * @param obj
	 *            对象实例
	 * @param out
	 *            输出流
	 */
	public static void writeObjectToFile(Object obj, OutputStream out) {
		ObjectOutputStream objOut = null;
		try {
			objOut = new ObjectOutputStream(out);
			objOut.writeObject(obj);
		} catch (Exception ex) {
		} finally {
			try {
				objOut.close();
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * 从指定文件中读入对象数据
	 * 
	 * @param path
	 *            文件路径
	 * @return Object 读入的对象实例
	 */
	public static Object readObjectFromFile(String path) {
		return readObjectFromFile(new File(path));
	}

	/**
	 * 从指定文件中读入对象数据
	 * 
	 * @param path
	 *            文件路径
	 * @return Object
	 */
	public static Object readObjectFromFile(File path) {
		FileInputStream in = null;
		Object obj = null;
		try {
			in = new FileInputStream(path);
			obj = readObjectFromFile(in);
		} catch (Exception ex) {
		} finally {
			try {
				in.close();
			} catch (Exception ex) {
			}
		}
		return obj;
	}

	/**
	 * 从指定的流中读入对象实例
	 * 
	 * @param in
	 *            对象输入流
	 * @return Object 读入的对象实例
	 */
	public static Object readObjectFromFile(InputStream in) {
		ObjectInputStream objIn = null;
		Object obj = null;
		try {
			objIn = new ObjectInputStream(in);
			obj = objIn.readObject();
		} catch (Exception ex) {
		} finally {
			try {
				objIn.close();
			} catch (Exception ex) {
			}
		}
		return obj;
	}

	/**
	 * 文件同名移动到一个目录或者改名为任意目录下的文件
	 * 
	 * @param input
	 *            要移动的源文件
	 * @param output
	 *            目标文件或目标目录
	 * @return 成功移动为true 否则为false
	 * @throws java.lang.Exception
	 * @author Liu WenMin
	 */
	public static boolean move(String input, String output) throws Exception {
		boolean bl = false;

		/* 判断源文件是否存在 */
		File inputFile = new File(input);
		if (!inputFile.exists()) {
			throw new Exception("源文件不存在!");
		}

		File outputFile = new File(output);

		/* 目标存在 */
		if (outputFile.exists()) {
			/* 如果是目录 将源文件移到这个目录下 */
			if (outputFile.isDirectory()) {
				outputFile = new File(output, inputFile.getName());
			} else {
				/* 如果存在同名的文件 先删除 */
				outputFile.delete();
				// throw new Exception("目标处已经存在同名文件！");

			}
		}

		/* 目标不存在 以上级为目录 最后一个为文件名 改名移动 */
		else {
			File parentFile = outputFile.getParentFile();
			parentFile.mkdirs();
			outputFile = new File(parentFile, outputFile.getName());
		}

		bl = inputFile.renameTo(outputFile);
		return bl;
	}

	/**
	 * 将文件移动到指定目录下，目录不存在，新建目录再移动
	 * 
	 * @param output
	 * @return
	 * @throws Exception
	 */
	public static boolean moveFileTodirectory(File inputFile, String output)
			throws Exception {
		boolean bl = false;
		/* 判断源文件是否存在 */
		if (!inputFile.exists()) {
			throw new Exception("源文件不存在!");
		}
		File outputFile = new File(output);
		/* 目标存在 */
		if (outputFile.exists()) {
			/* 如果是目录 将源文件移到这个目录下 */
			if (outputFile.isDirectory()) {

			} else {
				/* 如果存在同名的文件 先删除 */
				outputFile.delete();
				// throw new Exception("目标处已经存在同名文件！");
			}
		} else {
			outputFile.mkdirs();
		}
		outputFile = new File(output, inputFile.getName());
		bl = inputFile.renameTo(outputFile);
		return bl;
	}

	/**
	 * 文件拷贝
	 * 
	 * @param input
	 *            要拷贝的文件
	 * @param output
	 *            拷贝到的文件
	 * @return 正确拷贝，返回true
	 * @throws Exception
	 *             当拷贝发生错误时给出异常
	 */
	public static boolean copy(String input, String output) throws Exception {
		int BUFSIZE = 65536;
		FileInputStream fis = new FileInputStream(input);
		FileOutputStream fos = new FileOutputStream(output);

		try {
			int s;
			byte[] buf = new byte[BUFSIZE];
			while ((s = fis.read(buf)) > -1) {
				fos.write(buf, 0, s);
			}
		} catch (Exception ex) {
			throw new Exception("Can not copy file:" + input + " to " + output
					+ ex.getMessage());
		} finally {
			fis.close();
			fos.close();
		}
		return true;
	}

	/**
	 * @param home
	 * @throws Exception
	 */
	public static void makehome(String home) throws Exception {
		File homedir = new File(home);
		if (!homedir.exists()) {
			try {
				homedir.mkdirs();
			} catch (Exception ex) {
				throw new Exception("Can not mkdir :" + home
						+ " Maybe include special charactor!");
			}
		}
	}

	/**
	 * copies an input files of a directory to another directory not include
	 * subdir
	 * 
	 * @param sourcedir
	 *            the directory to copy from such as:/home/bqlr/images
	 * @param destdir
	 *            the target directory
	 * @throws Exception
	 */
	public static void copyDir(String sourcedir, String destdir)
			throws Exception {
		File dest = new File(destdir);
		File source = new File(sourcedir);

		String[] files = source.list();
		try {
			makehome(destdir);
		} catch (Exception ex) {
			throw new Exception("CopyDir:" + ex.getMessage());
		}

		for (int i = 0; i < files.length; i++) {
			String sourcefile = source + File.separator + files[i];
			String destfile = dest + File.separator + files[i];
			File temp = new File(sourcefile);
			if (temp.isFile()) {
				try {
					copy(sourcefile, destfile);
				} catch (Exception ex) {
					throw new Exception("CopyDir:" + ex.getMessage());
				}
			}
		}
	}

	/**
	 * 拷贝指定目录下的所有文件到目标目录,可以递归地拷贝子目录
	 * 
	 * @param src
	 *            源目录
	 * @param dest
	 *            目标目录
	 */
	public static void copyDirectory(String src, String dest)
			throws IOException {
		copyDirectory(new File(src), new File(dest));
	}

	public static void copyDirectory(String src, String dest, boolean isDeletSrc)
			throws IOException {
		copyDirectory(new File(src), new File(dest), isDeletSrc);
	}

	/**
	 * 移到源目录下的所有文件到目标目录下
	 * 
	 * @param src
	 *            源目录
	 * @param dest
	 *            目标目录
	 * @throws IOException
	 */
	public static void moveDirectory(String src, String dest)
			throws IOException {
		moveDirectory(new File(src), new File(dest));
	}
	/**
	 * 方法重载 移动文件到指定目录，过滤指定文件名
	 * @param src
	 * @param dest
	 * @param filterName
	 * @throws IOException
	 */
	public static void moveDirectory(String src, String dest, String filterName)
			throws IOException {
		moveDirectory(new File(src), new File(dest),filterName);
	}

	/**
	 * 移到源目录下的所有文件到目标目录下
	 * 
	 * @param src
	 *            源目录
	 * @param dest
	 *            目标目录
	 * @throws IOException
	 */
	public static void moveDirectory(File src, File dest) throws IOException {
		copyDirectory(src, dest, true);
	}
	/**
	 * 方法重载 移动目录下的所有文件到指定目录下
	 * @param src
	 * @param dest
	 * @param filterName
	 * @throws IOException
	 */
	public static void moveDirectory(File src, File dest, String filterName) throws IOException {
		copyDirectory(src, dest, true,filterName);
	}

	/**
	 * 拷贝指定目录下的所有文件到目标目录,可以递归地拷贝子目录
	 * 
	 * @param src
	 *            源目录
	 * @param dest
	 *            目标目录
	 */
	public static void copyDirectory(File src, File dest) throws IOException {
		copyDirectory(src, dest, false);
	}
	
	/**
	 * 拷贝指定目录下的所有文件到目标目录,可以递归地拷贝子目录,过滤文件
	 * 
	 * @param src
	 *            源目录
	 * @param dest
	 *            目标目录
	 */
	public static void copyDirectory(String src, String dest, boolean isDeletSrc, String filterName)
			throws IOException {
		copyDirectory(new File(src), new File(dest), isDeletSrc,filterName);
	}

	/**
	 * 拷贝指定目录下的所有文件到目标目录,可以递归地拷贝子目录
	 * 
	 * @param src
	 *            源目录
	 * @param dest
	 *            目标目录
	 * @param isDeleteSrc
	 *            是否删除源
	 */
	public static void copyDirectory(File src, File dest, boolean isDeleteSrc)
			throws IOException {
		if (src == null || dest == null)
			throw new IOException("源目录或目标目录不正确");
		if (!src.isDirectory() || !dest.isDirectory())
			throw new IOException("源或目标有可能不是目录形式");
		File[] srcFiles = src.listFiles();
		if (srcFiles != null) {
			for (int i = 0, n = srcFiles.length; i < n; i++) {
				String srcFilePath = srcFiles[i].getAbsolutePath();
				String fileName = srcFiles[i].getName();
				String destFilePath = dest.getAbsolutePath() + File.separator
						+ fileName;
				boolean isDir = srcFiles[i].isDirectory();
				if (isDir) {
					makeDirs(destFilePath);
					copyDirectory(srcFilePath, destFilePath, isDeleteSrc);
				} else {
					try {
							copy(srcFilePath, destFilePath);
							if (isDeleteSrc)
								srcFiles[i].delete();
					} catch (Exception e) {
						throw new IOException("文件拷贝异常,源文件:" + srcFilePath
								+ ",目标文件:" + destFilePath);
					}
				}
			}
		}
	}
	
	/**
	 * 方法重载，复制目录下所有文件夹到指定目录，可过滤指定文件
	 * 
	 * @param src
	 * @param dest
	 * @param isDeleteSrc
	 * @throws IOException
	 */

	public static void copyDirectory(File src, File dest, boolean isDeleteSrc, String filterName)
			throws IOException {
		if (src == null || dest == null)
			throw new IOException("源目录或目标目录不正确");
		if (!src.isDirectory() || !dest.isDirectory())
			throw new IOException("源或目标有可能不是目录形式");
		File[] srcFiles = src.listFiles();
		if (srcFiles != null) {
			for (int i = 0, n = srcFiles.length; i < n; i++) {
				String srcFilePath = srcFiles[i].getAbsolutePath();
				String fileName = srcFiles[i].getName();
				String destFilePath = dest.getAbsolutePath() + File.separator
						+ fileName;
				boolean isDir = srcFiles[i].isDirectory();
				if (isDir) {
					makeDirs(destFilePath);
					copyDirectory(srcFilePath, destFilePath, isDeleteSrc,filterName);
				} else {
					try {
						if(!fileName.equals(filterName)){
							copy(srcFilePath, destFilePath);
							if (isDeleteSrc)
								srcFiles[i].delete();
						}
					} catch (Exception e) {
						throw new IOException("文件拷贝异常,源文件:" + srcFilePath
								+ ",目标文件:" + destFilePath);
					}
				}
			}
			deleteDiretory(src);
		}
	}

	private static void deleteDiretory(File src) {
		File[] srcFiles = src.listFiles();
		if(srcFiles.length==0){
			deleteDir(src);
		}
	}

	/**
	 * del a directory recursively,that means delete all files and directorys.
	 * 
	 * @param directory
	 *            the directory that will be deleted.
	 * @throws Exception
	 */
	public static void recursiveRemoveDir(File directory) throws Exception {
		if (!directory.exists()) {
			throw new IOException(directory.toString() + " do not exist!");
		}

		String[] filelist = directory.list();
		File tmpFile = null;
		for (int i = 0; i < filelist.length; i++) {
			tmpFile = new File(directory.getAbsolutePath(), filelist[i]);
			if (tmpFile.isDirectory()) {
				recursiveRemoveDir(tmpFile);
			} else if (tmpFile.isFile()) {
				try {
					tmpFile.delete();
				} catch (Exception ex) {
					throw new Exception(tmpFile.toString()
							+ " can not be deleted " + ex.getMessage());
				}
			}
		}
		try {
			directory.delete();
		} catch (Exception ex) {
			throw new Exception(directory.toString() + " can not be deleted "
					+ ex.getMessage());
		} finally {
			filelist = null;
		}
	}

	/**
	 * Returns a reference to a file with the specified name that is located
	 * somewhere on the classpath. The code for this method is an adaptation of
	 * code supplied by Dave Postill.
	 * 
	 * @param name
	 *            the filename.
	 * 
	 * @return a reference to a file or <code>null</code> if no file could be
	 *         found.
	 */
	public static File findFileOnClassPath(String name) {

		String classpath = System.getProperty("java.class.path");
		String pathSeparator = System.getProperty("path.separator");
		String fileSeparator = System.getProperty("file.separator");

		StringTokenizer tokenizer = new StringTokenizer(classpath,
				pathSeparator);

		while (tokenizer.hasMoreTokens()) {
			String pathElement = tokenizer.nextToken();

			File directoryOrJar = new File(pathElement);
			File absoluteDirectoryOrJar = directoryOrJar.getAbsoluteFile();

			if (absoluteDirectoryOrJar.isFile()) {
				File target = new File(absoluteDirectoryOrJar.getParent()
						+ fileSeparator + name);
				if (target.exists()) {
					return target;
				}
			} else {
				File target = new File(pathElement + fileSeparator + name);
				if (target.exists()) {
					return target;
				}
			}

		}
		return null;

	}

	public static boolean fileExists(String _sPathFileName) {
		File file = new File(_sPathFileName);
		return file.exists();
	}

	public static boolean pathExists(String _sPathFileName) {
		String sPath = extractFilePath(_sPathFileName);
		return fileExists(sPath);
	}

	public static String extractFileName(String _sFilePathName) {
		int nPos = _sFilePathName.lastIndexOf(File.separatorChar);
		return _sFilePathName.substring(nPos + 1);
	}

	public static String extractHttpFileName(String _sFilePathName) {
		int nPos = _sFilePathName.lastIndexOf("/");
		return _sFilePathName.substring(nPos + 1);
	}

	public static String extractFileExt(String _sFilePathName) {
		int nPos = _sFilePathName.lastIndexOf('.');
		return nPos < 0 ? "" : _sFilePathName.substring(nPos + 1);
	}

	public static String extractFilePath(String _sFilePathName) {
		int nPos = _sFilePathName.lastIndexOf(File.separatorChar);
		return nPos < 0 ? "" : _sFilePathName.substring(0, nPos + 1);
	}

	public static String toAbsolutePathName(String _sFilePathName) {
		File file = new File(_sFilePathName);
		return file.getAbsolutePath();
	}

	public static String extractFileDrive(String _sFilePathName) {
		int nLen = _sFilePathName.length();
		if (nLen > 2 && _sFilePathName.charAt(1) == ':')
			return _sFilePathName.substring(0, 2);
		if (nLen > 2 && _sFilePathName.charAt(0) == File.separatorChar
				&& _sFilePathName.charAt(1) == File.separatorChar) {
			int nPos = _sFilePathName.indexOf(File.separatorChar, 2);
			if (nPos >= 0)
				nPos = _sFilePathName.indexOf(File.separatorChar, nPos + 1);
			return nPos < 0 ? _sFilePathName : _sFilePathName
					.substring(0, nPos);
		} else {
			return "";
		}
	}

	public static void down(HttpServletResponse response, String path,
                            String filename) throws Exception {
		File downloadFile = new File(path);
		InputStream in =null;
		OutputStream out=null;
		try {
			response.setHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(filename, "utf-8"));
			in= new FileInputStream(downloadFile);
			out = response.getOutputStream();
			byte[] bytes = new byte[1024];
			int length;
			while ((length = in.read(bytes, 0, bytes.length)) > 0) {
				out.write(bytes, 0, length);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			in.close();
			out.flush();
			out.close();
		}
	}

	public static void main(String[] args) throws Exception {
		//FileUtils.move("d:/a.txt", "e:/a.txt");
		Set<String> test=new HashSet<String>();
		test.add("JPG");
		System.out.println(test.contains("jpg"));
	}
}
