package core.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConfigStorage {

	private static final String UTF8_ENCODING = "utf8";
	private static final String EXT = ".config";
	private final File storagePath;

	public ConfigStorage(File storagePath) {
		if (storagePath == null)
			throw new NullPointerException("storagePath is null");
		this.storagePath = storagePath;
		this.storagePath.mkdirs();
		System.out.println("ConfigStorage::ConfigStorage.storagePath = " + this.storagePath.getAbsolutePath());
	}

	public ConfigStorage(String storagePath) {
		if (storagePath == null)
			throw new NullPointerException("storagePath is null");
		this.storagePath = new File(storagePath);
		this.storagePath.mkdirs();
		System.out.println("ConfigStorage::ConfigStorage.storagePath = " + this.storagePath.getAbsolutePath());
	}

	public boolean checkConfig(String name) {
		File expectedFile = new File(storagePath, name + EXT);
		System.out.println("ConfigStorage::checkConfig.expectedFile = " + expectedFile.getAbsolutePath());
		return expectedFile.exists();
	}

	public String readConfig(String name) {
		File expectedFile = new File(storagePath, name + EXT);
		System.out.println("ConfigStorage::readConfig.expectedFile = " + expectedFile.getAbsolutePath());
		if (!expectedFile.exists()) {
			return null;
		}
		try {
			byte[] readBuf = new byte[(int) expectedFile.length()];
			FileInputStream in = new FileInputStream(expectedFile);
			in.read(readBuf);
			in.close();
			return new String(readBuf, UTF8_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void writeConfig(String name, String data) {
		File expectedFile = new File(storagePath, name + EXT);
		System.out.println("ConfigStorage::writeConfig.expectedFile = " + expectedFile.getAbsolutePath());
		try {
			if (!expectedFile.exists()) {
				expectedFile.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(expectedFile);
			out.write(data.getBytes(UTF8_ENCODING));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new AssertionError("Failed to write configuration file " + expectedFile.getAbsolutePath());
		}
	}
}
