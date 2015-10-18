package net.ozsofts.wechat.core.functions;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import net.ozsofts.wechat.core.WechatCommService;

/**
 * <p>
 * 临时素材功能接口
 * 
 * @author Jack
 * 
 */
public class MediaFunction {
	/**
	 * <p>
	 * 新增临时素材
	 * 
	 * @param token
	 * @param type
	 * @param mediaFile
	 *            需要上传的素材文件
	 * @return
	 */
	public String uploadMedia(String token, String type, File mediaFile) throws Exception {
		Map<String, String> params = new TreeMap<String, String>();
		params.put("type", type);

		return WechatCommService.instance().postMedia("/cgi-bin/media/upload", token, params, null, mediaFile);
	}

	/**
	 * <p>
	 * 获取临时素材文件
	 * 
	 * @param token
	 * @param mediaId
	 * @param mediaFile
	 *            需要保存素材的文件
	 * @throws Exception
	 */
	public void getMedia(String token, String mediaId, File mediaFile) throws Exception {
		Map<String, String> params = new TreeMap<String, String>();
		params.put("media_id", mediaId);

		WechatCommService.instance().getMedia("/cgi-bin/media/get", token, mediaId, mediaFile);
	}
}
