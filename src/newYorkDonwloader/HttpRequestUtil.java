package newYorkDonwloader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HTTP请求工具
 * 
 * @author webber_dy
 */
public class HttpRequestUtil {
	/**
	 * 发起post请求并获取结果
	 * 
	 * @param path url
	 * @return
	 */
	public static Detect401 postDownTerrain(String path, File file) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(5000);
			httpURLConnection.setReadTimeout(5000);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
			if (httpURLConnection.getResponseCode() == 404) {
				return new Detect401(404);
			}
			if (httpURLConnection.getResponseCode() == 401) {
				return new Detect401(401);
			}
			if (httpURLConnection.getResponseCode() == 503) {
				return new Detect401(503);
			}
			if (httpURLConnection.getResponseCode() == 500) {
				return new Detect401(500);
			}
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
			FileOutputStream bos = new FileOutputStream(file);
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();
			return new Detect401();
		} catch (Exception e) {
			if (e.getMessage().equals("Read timed out") || e.getMessage().equals("Connection reset")
					|| e.getMessage().equals("connect timed out")) {
				return new Detect401(300);
			} else if (e.getMessage().equals("assets.cesium.com")) {
				return new Detect401(401);
			} else {
				System.err.println(e.toString());
				// e.printStackTrace();
			}
		}
		return null;
	}

	public static byte[] getAT(String path) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(2500);
			httpURLConnection.setReadTimeout(2500);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();
			return bos.toByteArray();
		} catch (Exception e) {
			if (e.getMessage().equals("Read timed out") || (e.getMessage().equals("connect timed out"))) {
				return null;
			} else {
				System.err.println(e.toString());
				// e.printStackTrace();
			}
		}
		return null;
	}

}
