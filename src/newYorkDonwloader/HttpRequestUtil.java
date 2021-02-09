package newYorkDonwloader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HTTP请求工具
 * 
 * @author webber_dy
 */
public class HttpRequestUtil {

	public static Detect401 postDownTerrain(String path, File file) {
		return HttpRequestUtil.postDownTerrain(path, "", file);
	}

	/**
	 * 发起post请求并获取结果
	 * 
	 * @param path url
	 * @return
	 */
	public static Detect401 postDownTerrain(String path, String token, File file) {
		URL url = null;
		try {
			System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestProperty("Referer", "https://sandcastle.cesium.com/");
			httpURLConnection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiI4YzhlYmM2OC1jZjQ0LTRhMTgtOWY1MS1jNmFiMjUxNjI1YjMiLCJpZCI6MjU5LCJhc3NldHMiOnsiNDA4NjYiOnsidHlwZSI6IjNEVElMRVMifX0sInNyYyI6IjEzYzE2NDA4LTQ4MjMtNDg0My05NDE2LTFkMmNkOTRhZjAwNiIsImlhdCI6MTYxMjgzMTA0NiwiZXhwIjoxNjEyODM0NjQ2fQ.5OtuCsg2FUhPmxksSdsrJO5TavzM96CAWzUZPuKW3mk");
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(0);
			httpURLConnection.setReadTimeout(0);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.addRequestProperty("User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64 ) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
			
			int ResponseCode = httpURLConnection.getResponseCode();
			switch (ResponseCode) {
			case 200:
				break;
			case 404:
				return new Detect401(ResponseCode);
			case 401:
				return new Detect401(ResponseCode);
			case 503:
				return new Detect401(ResponseCode);
			case 500:
				return new Detect401(ResponseCode);
			case 300:
				httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setRequestMethod("GET");
				httpURLConnection.setConnectTimeout(0);
				httpURLConnection.setReadTimeout(0);
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setDoInput(true);
				httpURLConnection.addRequestProperty("User-Agent",
						"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
				httpURLConnection.addRequestProperty("accept", "*/*;" + token);
				httpURLConnection.addRequestProperty("accept-encoding", "gzip");
				break;
			default:
				Map<String, List<String>> tmp = httpURLConnection.getHeaderFields();
				Set<String> keys = tmp.keySet();
				Iterator<String> a = keys.iterator();
				while (a.hasNext()) {
					String key = (String) a.next();
					System.out.println(key + ":" + tmp.get(key));
				}
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
				return new Detect401(0);
			} else if (e.getMessage().equals("assets.cesium.com")) {
				return new Detect401(1);
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
