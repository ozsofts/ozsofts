package net.ozsofts.wechat.core.service.actevent;

import java.util.ArrayList;
import java.util.List;

import net.ozsofts.wechat.core.message.type.ReceiveMessage;
import net.ozsofts.wechat.core.message.type.ResponseMessage;
import net.ozsofts.wechat.core.models.WXAccount;
import net.ozsofts.wechat.core.models.WXActEvent;

/**
 * <p>
 * 对微信活动进行处理
 * 
 * @author jack
 */
public class ActEventService {
	// private static Logger log = LoggerFactory.getLogger(ActEventService.class);

	/** 保存系统中最大的活动标识，在新的查询时只需要查询这个值以后的即可 */
	private static Long maxEventId = 0l;

	private static List<ActEventController> controllerList = new ArrayList<ActEventController>();

	// 判断上行的消息是否与当前的活动相关
	public static ResponseMessage handleMessage(WXAccount wxaccount, ReceiveMessage message) throws Exception {

		// 首先处理缓存中的活动控制对象
		for (ActEventController controller : controllerList) {
			if (controller.check(wxaccount.getLong("id"), message)) {
				return controller.handleMessage(message);
			}
		}

		// 如果缓存中没有匹配的活动，再从数据库中读取
		List<WXActEvent> actevents = WXActEvent.dao.findAllEvents(wxaccount.getLong("id"), maxEventId);
		if (!actevents.isEmpty()) {
			List<ActEventController> newControllerList = new ArrayList<ActEventController>();
			for (WXActEvent actevent : actevents) {
				ActEventController controller = new ActEventController(actevent);
				controller.initialize();

				controllerList.add(controller);
				newControllerList.add(controller);
			}
			maxEventId = actevents.get(actevents.size() - 1).getLong("id"); // 保存最大的活动标识

			for (ActEventController controller : newControllerList) {
				if (controller.check(wxaccount.getLong("id"), message)) {
					return controller.handleMessage(message);
				}
			}
		}

		return null;
	}
}
