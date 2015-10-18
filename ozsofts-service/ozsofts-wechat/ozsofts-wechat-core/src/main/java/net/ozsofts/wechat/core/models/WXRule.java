package net.ozsofts.wechat.core.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Model;

public class WXRule extends Model<WXRule> {
	private static final long serialVersionUID = -6826195256951458565L;

	public static final WXRule dao = new WXRule();

	//
	// 规则类型定义
	//
	/** 关注时自动回复 */
	public static final String RULE_TYPE_DEFAULT = "default";
	/** 无匹配消息时自动回复 */
	public static final String RULE_TYPE_AUTO = "auto";
	/** 关键字匹配 */
	public static final String RULE_TYPE_KEYWORD = "keyword";

	//
	// 匹配类型
	//
	/** 正则 */
	public static final String MATCH_TYPE_REGEX = "regex";
	/** 以关键字开头 */
	public static final String MATCH_TYPE_STARTSWITH = "startsWith";
	/** 以关键字结尾 */
	public static final String MATCH_TYPE_ENDSWITH = "endsWith";
	/** 包含关键字 */
	public static final String MATCH_TYPE_CONTAINS = "contains";

	public List<WXRule> findRules(long accountId, String ruleType, String refType, long refId) {
		List<Object> params = new ArrayList<Object>();
		params.add(accountId);
		params.add(StringUtils.isNotBlank(ruleType) ? ruleType : RULE_TYPE_AUTO);
		params.add(StringUtils.isBlank(refType) ? "" : refType);

		StringBuilder sql = new StringBuilder("select * from t_wx_rules where account=? and rule_type=? and ref_type=?");
		if (StringUtils.isNotBlank(refType) && refId > 0l) {
			sql.append(" and ref_id=?");
			params.add(refId);
		}
		return find(sql.toString(), params.toArray());
	}
}
