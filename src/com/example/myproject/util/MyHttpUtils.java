package com.example.myproject.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

public class MyHttpUtils {

	/**
	 * ajax返回Map为Json
	 * 
	 * @param client
	 * @param url
	 * @return
	 */
	public  List getJson(HttpClient client, String url) {
		HttpGet get = new HttpGet(url);
		List map = null;
		try {
			long startTime = System.currentTimeMillis(); // 获取开始时间
			HttpResponse res = client.execute(get);
			long endTime = System.currentTimeMillis(); // 获取结束时间

			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = res.getEntity();
				String strEntity = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);

		} finally {
			// 关闭连接 ,释放资源
			// client.getConnectionManager().shutdown();
		}
		return map;
	}
	
	public static String getContext(HttpClient client, String url)
			throws ClientProtocolException, IOException {
		
		HttpGet get = new HttpGet(url);
		HttpResponse res = client.execute(get);
		StringBuffer sb = new StringBuffer();
		if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = res.getEntity();
			InputStream is = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			try {
				String str = br.readLine();
				while (str != null) {
					sb.append(str + (char) 13 + (char) 10);
					str = br.readLine();
				}
			} catch (Exception e) {
			} finally {
				try {
					is.close();
					get.abort();
				} catch (Exception ignore) {
				}
			}
		}
		return sb.toString();
	}
	
}
