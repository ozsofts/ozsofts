package net.ozsofts.wechat.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ozsofts.wechat.core.servlet.TokenService;
import net.ozsofts.wechat.utils.MySecureProtocolSocketFactory;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 访问微信接口的处理类，此类对访问微信的处理进行封装
 * <p>
 * 此类是工具方法，在整个处理中只有一个实例
 * 
 * @author jack
 */
public class WechatCommService {
	private static Logger trace = LoggerFactory.getLogger("wx.data");

	private static Logger log = LoggerFactory.getLogger(WechatCommService.class);

	private static WechatCommService instance = null;

	/** 连接超时时间 */
	private int connTimeout = 60000;
	/** 接收超时时间 */
	private int recvTimeout = 60000;

	private HttpClient httpClient;

	private static Object lock = new Object();

	// 对此实例进行静态初始化
	public static WechatCommService instance() throws Exception {
		if (instance != null) {
			return instance;
		}

		synchronized (lock) {
			if (instance == null) {
				instance = new WechatCommService();
				instance.initialize();
			}
		}

		return instance;
	}

	public void initialize() throws Exception {
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		httpClient = new HttpClient(connectionManager);

		// 微信都是使用https协议
		ProtocolSocketFactory factory = new MySecureProtocolSocketFactory();
		Protocol https = new Protocol("https", factory, 443);
		httpClient.getHostConfiguration().setHost("api.weixin.qq.com", 443, https);

		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connTimeout);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(recvTimeout);
	}

	public JSONObject get(String funcUrl, String token, Map<String, String> params) throws Exception {
		long startTime = System.currentTimeMillis();
		StringBuilder tracesb = new StringBuilder("[GET : ").append(funcUrl).append("]\n");

		GetMethod getMethod = null;
		try {
			getMethod = new GetMethod(funcUrl);

			List<NameValuePair> nvpairList = new LinkedList<NameValuePair>();
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet()) {
					nvpairList.add(new NameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			nvpairList.add(new NameValuePair(TokenService.ACCESS_TOKEN, token));
			NameValuePair[] nvpairs = new NameValuePair[nvpairList.size()];
			getMethod.setQueryString(nvpairList.toArray(nvpairs));

			if (trace.isInfoEnabled()) {
				tracesb.append("  request  :[").append(getMethod.getQueryString()).append("]\n");
			}

			int status = httpClient.executeMethod(getMethod);
			if (status != HttpStatus.SC_OK) {
				log.error("[get method] status=[{}]", status);
				throw new Exception("[get method] status=[" + status + "]");
			}

			JSONObject result = JSONObject.fromObject(new String(getMethod.getResponseBody()));
			if (result.containsKey(Constants.PARA_ERR_CODE)) {
				String errorCode = result.getString(Constants.PARA_ERR_CODE);
				if (!"0".equals(errorCode)) {
					log.error("[get method] error code:[{}]  error message:[{}]", errorCode, Error.getErrorMessage(errorCode));
					throw new Exception("[get method] error code:[" + errorCode + "]  error message:[" + Error.getErrorMessage(errorCode) + "]");
				}
			}

			if (trace.isInfoEnabled()) {
				tracesb.append("  response :[").append(result.toString()).append("]\n");
				tracesb.append("[GET : ").append(funcUrl).append("  time:").append(System.currentTimeMillis() - startTime).append("]");
				trace.info(tracesb.toString());
			}

			return result;
		} catch (Exception ex) {
			if (trace.isInfoEnabled()) {
				tracesb.append("  error    :[").append(ex.getMessage()).append("]\n");
				tracesb.append("[GET : ").append(funcUrl).append("  time:").append(System.currentTimeMillis() - startTime).append("]");
				trace.info(tracesb.toString());
			}

			log.error("[get method] communication error!", ex);
			throw ex;
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection();
			}
		}
	}

	public void getMedia(String funcUrl, String token, String mediaId, File recvFile) throws Exception {
		long startTime = System.currentTimeMillis();
		StringBuilder tracesb = new StringBuilder("[POST: ").append(funcUrl).append("]\n");

		BufferedOutputStream bos = null;
		PostMethod postMethod = null;
		try {
			postMethod = new PostMethod(funcUrl);
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

			// 设置URL参数
			List<NameValuePair> nvpairList = new LinkedList<NameValuePair>();
			nvpairList.add(new NameValuePair(Constants.PARA_ACCESS_TOKEN, token));
			NameValuePair[] nvpairs = new NameValuePair[nvpairList.size()];
			postMethod.setQueryString(nvpairList.toArray(nvpairs));
			if (trace.isInfoEnabled()) {
				tracesb.append("  request  :\n").append("    param:[").append(postMethod.getQueryString()).append("]\n");
			}

			// 设置POST的数据信息
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("media_id", mediaId);
			JSONObject json = JSONObject.fromObject(data);
			StringRequestEntity requestEntity = new StringRequestEntity(json.toString(), "application/json", "UTF-8");
			postMethod.setRequestEntity(requestEntity);
			if (trace.isInfoEnabled()) {
				tracesb.append("    data :[").append(json.toString()).append("]\n");
			}

			int status = httpClient.executeMethod(postMethod);
			if (status != HttpStatus.SC_OK) {
				log.error("[get media post method] status=[" + status + "]");
				throw new Exception("[get media post method] status=[" + status + "]");
			}

			InputStream in = postMethod.getResponseBodyAsStream();
			bos = new BufferedOutputStream(new FileOutputStream(recvFile));

			byte[] b = new byte[1024];
			int len = 0;
			int length = 0;
			while ((len = in.read(b)) != -1) {
				bos.write(b, 0, len);

				length += len;
			}

			if (trace.isInfoEnabled()) {
				tracesb.append("  response :[ media length=").append(length).append("]\n");
				tracesb.append("[POST: ").append(funcUrl).append("  time:").append(System.currentTimeMillis() - startTime).append("]");
				trace.info(tracesb.toString());
			}
		} catch (Exception ex) {
			if (trace.isInfoEnabled()) {
				tracesb.append("  error    :[").append(ex.getMessage()).append("]\n");
				tracesb.append("[POST: ").append(funcUrl).append("  time:").append(System.currentTimeMillis() - startTime).append("]");
				trace.info(tracesb.toString());
			}

			log.error("[get media post method] communication error!", ex);
			throw ex;
		} finally {
			if (bos != null) {
				IOUtils.closeQuietly(bos);
			}

			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
	}

	public JSONObject post(String funcUrl, String token, Map<String, String> params, Map<String, Object> data) throws Exception {
		long startTime = System.currentTimeMillis();
		StringBuilder tracesb = new StringBuilder("[POST: ").append(funcUrl).append("]\n");

		PostMethod postMethod = null;
		try {
			postMethod = new PostMethod(funcUrl);
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

			// 设置URL参数
			List<NameValuePair> nvpairList = new LinkedList<NameValuePair>();
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet()) {
					nvpairList.add(new NameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			nvpairList.add(new NameValuePair(Constants.PARA_ACCESS_TOKEN, token));
			NameValuePair[] nvpairs = new NameValuePair[nvpairList.size()];
			postMethod.setQueryString(nvpairList.toArray(nvpairs));

			if (trace.isInfoEnabled()) {
				tracesb.append("  request  :\n").append("    param:[").append(postMethod.getQueryString()).append("]\n");
			}

			// 设置POST的数据信息
			if (data != null && !data.isEmpty()) {
				JSONObject json = JSONObject.fromObject(data);
				StringRequestEntity requestEntity = new StringRequestEntity(json.toString(), "application/json", "UTF-8");
				postMethod.setRequestEntity(requestEntity);

				if (trace.isInfoEnabled()) {
					tracesb.append("    data :[").append(json.toString()).append("]\n");
				}
			}

			int status = httpClient.executeMethod(postMethod);
			if (status != HttpStatus.SC_OK) {
				log.error("[post method] status=[" + status + "]");
				throw new Exception("[post method] status=[" + status + "]");
			}

			JSONObject result = JSONObject.fromObject(new String(postMethod.getResponseBody()));
			if (result.containsKey(Constants.PARA_ERR_CODE)) {
				String errorCode = result.getString(Constants.PARA_ERR_CODE);
				if (!"0".equals(errorCode)) {
					log.error("[post method] error code:[" + errorCode + "]  error message:[" + Error.getErrorMessage(errorCode) + "]");
					throw new Exception("[post method] error code:[" + errorCode + "]  error message:[" + Error.getErrorMessage(errorCode) + "]");
				}
			}

			if (trace.isInfoEnabled()) {
				tracesb.append("  response :[").append(result.toString()).append("]\n");
				tracesb.append("[POST: ").append(funcUrl).append("  time:").append(System.currentTimeMillis() - startTime).append("]");
				trace.info(tracesb.toString());
			}

			return result;
		} catch (Exception ex) {
			if (trace.isInfoEnabled()) {
				tracesb.append("  error    :[").append(ex.getMessage()).append("]\n");
				tracesb.append("[POST: ").append(funcUrl).append("  time:").append(System.currentTimeMillis() - startTime).append("]");
				trace.info(tracesb.toString());
			}

			log.error("[post method] communication error!", ex);
			throw ex;
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
	}

	public String postMedia(String funcUrl, String token, Map<String, String> params, Map<String, Object> data, File file) throws Exception {
		long startTime = System.currentTimeMillis();
		StringBuilder tracesb = new StringBuilder("[POST: ").append(funcUrl).append("]\n");

		PostMethod postMethod = null;
		try {
			postMethod = new PostMethod(funcUrl);
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

			// 设置URL参数
			List<NameValuePair> nvpairList = new LinkedList<NameValuePair>();
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet()) {
					nvpairList.add(new NameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			nvpairList.add(new NameValuePair(Constants.PARA_ACCESS_TOKEN, token));
			NameValuePair[] nvpairs = new NameValuePair[nvpairList.size()];
			postMethod.setQueryString(nvpairList.toArray(nvpairs));
			if (trace.isInfoEnabled()) {
				tracesb.append("  request  :\n").append("    param:[").append(postMethod.getQueryString()).append("]\n");
			}

			List<Part> partList = new LinkedList<Part>();
			// 设置POST的数据信息
			if (data != null && !data.isEmpty()) {
				for (Entry<String, Object> d : data.entrySet()) {
					JSONObject jdata = JSONObject.fromObject(d.getValue());
					partList.add(new StringPart(d.getKey(), jdata.toString(), "UTF-8"));
				}

				if (trace.isInfoEnabled()) {
					tracesb.append("    data :[").append(data.toString()).append("]\n");
				}
			}
			partList.add(new FilePart("media", file.getName(), file));
			Part[] parts = new Part[partList.size()];
			MultipartRequestEntity mrp = new MultipartRequestEntity(partList.toArray(parts), postMethod.getParams());
			postMethod.setRequestEntity(mrp);

			int status = httpClient.executeMethod(postMethod);
			if (status != HttpStatus.SC_OK) {
				log.error("[postForm method] status=[" + status + "]");
				throw new Exception("[post method for media] status=[" + status + "]");
			}

			JSONObject result = JSONObject.fromObject(new String(postMethod.getResponseBody()));
			if (result.containsKey(Constants.PARA_ERR_CODE)) {
				String errorCode = result.getString(Constants.PARA_ERR_CODE);
				if (!"0".equals(errorCode)) {
					log.error("[postForm method] error code:[" + errorCode + "]  error message:[" + Error.getErrorMessage(errorCode) + "]");
					throw new Exception("[postForm method] error code:[" + errorCode + "]  error message:[" + Error.getErrorMessage(errorCode) + "]");
				}
			}

			if (trace.isInfoEnabled()) {
				tracesb.append("  response :[").append(result.toString()).append("]\n");
				tracesb.append("[POST: ").append(funcUrl).append("  time:").append(System.currentTimeMillis() - startTime).append("]");
				trace.info(tracesb.toString());
			}

			return result.getString("media_id");
		} catch (Exception ex) {
			if (trace.isInfoEnabled()) {
				tracesb.append("  error    :[").append(ex.getMessage()).append("]\n");
				tracesb.append("[POST: ").append(funcUrl).append("  time:").append(System.currentTimeMillis() - startTime).append("]");
				trace.info(tracesb.toString());
			}

			log.error("[postForm method] communication error!", ex);
			throw ex;
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
	}
}
