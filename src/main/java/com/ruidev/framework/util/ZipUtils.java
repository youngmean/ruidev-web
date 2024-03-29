package com.ruidev.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.zip.ZipUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ZipUtils
 */
public class ZipUtils {

	protected final static Logger log = LogManager.getLogger(ZipUtil.class);
	private static final int BUFFER_SIZE = 2 * 1024;

	/**
	 * 解压
	 */
	public static void decompress(String zipFileName, String destPath) {
		final int BUFFER = 512;
		try {
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFileName));
			ZipEntry zipEntry = null;
			byte[] buffer = new byte[BUFFER];// 缓冲器
			int readLength = 0;// 每次读出来的长度
			while ((zipEntry = zis.getNextEntry()) != null) {
				if (zipEntry.isDirectory()) {// 若是目录
					File file = new File(destPath + "/" + zipEntry.getName());
					if (!file.exists()) {
						file.mkdirs();
						continue;
					}
				} // 若是文件
				File file = createFile(destPath, zipEntry.getName());
				OutputStream os = new FileOutputStream(file);
				while ((readLength = zis.read(buffer, 0, BUFFER)) != -1) {
					os.write(buffer, 0, readLength);
				}
				os.close();
			}
			zis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param destPath 解压目标路径
	 * @param fileName 解压文件的相对路径
	 */
	public static File createFile(String destPath, String fileName) {
		String[] dirs = fileName.split("/");// 将文件名的各级目录分解
		File file = new File(destPath);
		if (dirs.length > 1) {// 文件有上级目录
			for (int i = 0; i < dirs.length - 1; i++) {
				file = new File(file, dirs[i]);// 依次创建文件对象知道文件的上一级目录
			}
			if (!file.exists()) {
				file.mkdirs();// 文件对应目录若不存在，则创建
			}
			file = new File(file, dirs[dirs.length - 1]);// 创建文件
			return file;
		} else {
			if (!file.exists()) {// 若目标路径的目录不存在，则创建
				file.mkdirs();
			}
			file = new File(file, dirs[0]);// 创建文件
			return file;
		}
	}

	/**
	 * 压缩成ZIP 方法* @param srcDir 压缩文件夹路径
	 * 
	 * @param out              压缩文件输出流
	 * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
	 *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws RuntimeException 压缩失败会抛出运行时异常
	 */
	public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws RuntimeException {
		long start = System.currentTimeMillis();
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			File sourceFile = new File(srcDir);
			compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
			long end = System.currentTimeMillis();
			log.info("压缩完成，耗时：" + (end - start) + " ms");
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 压缩成ZIP 方法* @param srcFiles 需要压缩的文件列表
	 * 
	 * @param out 压缩文件输出流
	 * @throws RuntimeException 压缩失败会抛出运行时异常
	 */
	public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {
		long start = System.currentTimeMillis();
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			for (File srcFile : srcFiles) {
				byte[] buf = new byte[BUFFER_SIZE];
				zos.putNextEntry(new ZipEntry(srcFile.getName()));
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			}
			long end = System.currentTimeMillis();
			log.info("压缩完成，耗时：" + (end - start) + " ms");
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 递归压缩方法
	 * 
	 * @param sourceFile       源文件
	 * @param zos              zip输出流
	 * @param name             压缩后的名称
	 * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
	 *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws Exception
	 */
	private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure)
			throws Exception {
		byte[] buf = new byte[BUFFER_SIZE];
		if (sourceFile.isFile()) {
			// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
			zos.putNextEntry(new ZipEntry(name));
			// copy文件到zip输出流中
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}
			// Complete the entry
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				// 需要保留原来的文件结构时,需要对空文件夹进行处理
				if (KeepDirStructure) {
					// 空文件夹的处理
					zos.putNextEntry(new ZipEntry(name + "/"));
					// 没有文件，不需要文件的copy
					zos.closeEntry();
				}
			} else {
				for (File file : listFiles) {
					// 判断是否需要保留原来的文件结构
					if (KeepDirStructure) {
						// 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
						// 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
						compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
					} else {
						compress(file, zos, file.getName(), KeepDirStructure);
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		/** 测试压缩方法 */
		FileOutputStream fos1 = new FileOutputStream(new File("D:\\work\\tongjivms\\20180708\\2018-07-12.zip"));
		ZipUtils.toZip("D:\\work\\tongjivms\\20180708\\2018-07-12", fos1, true);
		/** 测试压缩方法 */
		/*
		 * List<File> fileList = new ArrayList<File>(); fileList.add(new
		 * File("D:/Java/jdk1.7.0_45_64bit/bin/jar.exe")); fileList.add(new
		 * File("D:/Java/jdk1.7.0_45_64bit/bin/java.exe")); FileOutputStream fos2 = new
		 * FileOutputStream(new File("c:/mytest02.zip")); ZipUtils.toZip(fileList,
		 * fos2);
		 */
	}
}